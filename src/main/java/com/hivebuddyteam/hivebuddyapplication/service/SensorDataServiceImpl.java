package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.controller.apiDTO.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
}
