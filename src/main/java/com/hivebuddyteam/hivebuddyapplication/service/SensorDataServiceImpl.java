package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.dto.SingleSensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SensorDataServiceImpl implements SensorDataService{

    private final SensorDataRepository sensorDataRepository;

    private final DeviceService deviceService;

    @Autowired
    public SensorDataServiceImpl(SensorDataRepository sensorDataRepository, DeviceService deviceService) {
        this.sensorDataRepository = sensorDataRepository;
        this.deviceService = deviceService;
    }

    @Override
    public List<SensorData> getAll() {
        return sensorDataRepository.findAll();
    }

    @Override
    public SensorData save(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    @Override
    public SensorData save(SensorDataDto sensorDataDto) {

        Device device = deviceService.findBySerial(sensorDataDto.getSerialNumber());

        if (device == null) {
            throw new RuntimeException("No registered device with serial - " + sensorDataDto.getSerialNumber());
        }

        LocalDateTime timestamp = LocalDateTime.now();

        SensorData newSensorData = new SensorData(
                device,
                timestamp,
                sensorDataDto.getSensor1(),
                sensorDataDto.getSensor2(),
                sensorDataDto.getSensor3(),
                sensorDataDto.getSensor4(),
                sensorDataDto.getSensor5()
        );

        return sensorDataRepository.save(newSensorData);
    }

    @Override
    public SensorData findLatestByDevice(Device device) {
        return sensorDataRepository.findTopByDeviceOrderByTimestampDesc(device);
    }

    @Override
    public List<SingleSensorDataDto> getSingleSensorDataWithFrequency(
            Device device,
            Integer sensorNumber,
            Integer minutes,
            Integer frequency
    ) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusMinutes(minutes);

        List<SensorData> sensorDataList = sensorDataRepository.findAllByDeviceAndTimestampBetween(device, startTime, endTime);

        List<SingleSensorDataDto> singleSensorDataDtoList = new ArrayList<>();

        for (int i = 0; i < minutes; i += frequency) {
            LocalDateTime intervalStart = startTime.plusMinutes(i);
            LocalDateTime intervalEnd = startTime.plusMinutes(i + frequency);

            List<SensorData> intervalData = sensorDataList.stream()
                    .filter(data -> data.getTimestamp().isAfter(intervalStart) && data.getTimestamp().isBefore(intervalEnd))
                    .toList();

            if (!intervalData.isEmpty()) {
                BigDecimal average = calculateAverage(intervalData, sensorNumber);

                SingleSensorDataDto dto = new SingleSensorDataDto(device.getSerialNumber(), intervalEnd, average);
                singleSensorDataDtoList.add(dto);
            }
        }

        return singleSensorDataDtoList;
    }

    private BigDecimal calculateAverage(List<SensorData> intervalData, Integer sensorNumber) {
        return intervalData.stream()
                .map(sensorData -> getSensorValue(sensorData, sensorNumber))
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(new BigDecimal(intervalData.size()), RoundingMode.HALF_UP);
    }

    private BigDecimal getSensorValue(SensorData sensorData, Integer sensorNumber) {
        return switch (sensorNumber) {
            case 1 -> sensorData.getSensor1();
            case 2 -> sensorData.getSensor2();
            case 3 -> sensorData.getSensor3();
            case 4 -> sensorData.getSensor4();
            case 5 -> sensorData.getSensor5();
            default -> throw new IllegalArgumentException("Invalid sensor number: " + sensorNumber);
        };
    }
}
