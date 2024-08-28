package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.dto.SingleSensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.NotificationService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sensorapi")
public class SensorDataRestController {

    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    private final NotificationService notificationService;

    public SensorDataRestController(SensorDataService sensorDataService, DeviceService deviceService, NotificationService notificationService) {
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
        this.notificationService = notificationService;
    }

    @GetMapping("/sensors")
    public List<SensorData> showData() {
        return sensorDataService.getAll();
    }

    @PostMapping("/sensors")
    public SensorDataDto saveData(@RequestBody SensorDataDto sensorDataDto) {

        Device device = deviceService.findBySerial(sensorDataDto.getSerialNumber());

        if (device == null) {
            return null;
        }

        if (!device.getActive()) {
            return null;
        }

        if (!device.getSecurityCode().equals(sensorDataDto.getSecurityCode())) {
            return null;
        }

        if (sensorDataDto.getSensor1().doubleValue() > 36) {
            notificationService.registerSensorNotification(
                    device,
                    "temp",
                    sensorDataDto.getSensor1().doubleValue()
            );
        }

        if (sensorDataDto.getSensor2().doubleValue() > 65) {
            notificationService.registerSensorNotification(
                    device,
                    "humid",
                    sensorDataDto.getSensor2().doubleValue()
            );
        }

        if (sensorDataDto.getSensor4().doubleValue() != 0) {
            notificationService.registerSensorNotification(
                    device,
                    "water",
                    sensorDataDto.getSensor4().doubleValue()
            );
        }

        if (sensorDataDto.getSensor5().doubleValue() != 0) {
            notificationService.registerSensorNotification(
                    device,
                    "noise",
                    sensorDataDto.getSensor5().doubleValue()
            );
        }

        SensorData newSensorData = sensorDataService.save(sensorDataDto);

        SensorDataDto newSensorDataDto = SensorDataDto.mapToDto(
                sensorDataDto.getSerialNumber(),
                newSensorData
        );

        return newSensorDataDto;
    }

    @GetMapping("/data/sensor/{serial}")
    public ResponseEntity<List<SingleSensorDataDto>> getSingleSensorData(
            @PathVariable String serial,
            @RequestParam("minutes") Integer minutes,
            @RequestParam("sensorNumber") Integer sensorNumber,
            @RequestParam("average") Boolean average, // option to add request for data without calculation of averages
            @RequestParam("frequency") Integer frequency
    ) {
        if (sensorNumber > 5) {
            return ResponseEntity.badRequest().build();
        }

        Device device = deviceService.findBySerial(serial);

        if (device == null) {
            return ResponseEntity.badRequest().build();
        }


        List<SingleSensorDataDto> dtoList = sensorDataService.getSingleSensorDataWithFrequency(device, sensorNumber, minutes, frequency);

        if (dtoList == null || dtoList.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(dtoList);
    }


    @GetMapping("/data/device/{serial}")
    public ResponseEntity<List<SensorDataDto>> getDeviceSensorData(
            @PathVariable String serial,
            @RequestParam("minutes") Integer minutes,
            @RequestParam("frequency") Integer frequency
    ) {
        Device device = deviceService.findBySerial(serial);

        if (device == null) {
            return ResponseEntity.badRequest().build();
        }

        List<SensorDataDto> dtoList = sensorDataService.getDeviceSensorDataWithFrequency(device, minutes, frequency);

        if (dtoList == null || dtoList.isEmpty()) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(dtoList);
    }

    @GetMapping("/data/device/{serial}/sensor/{sensor}")
    public ResponseEntity<List<SingleSensorDataDto>> getDeviceSingleSensorData(
            @PathVariable String serial,
            @PathVariable Integer sensor,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(value = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(value = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) {
        Device device = deviceService.findBySerial(serial);

        if (device == null) {
            return ResponseEntity.notFound().build();
        }

        if (date != null) {
            LocalDate now = LocalDate.now();
            if (date.isAfter(now)) {
                return ResponseEntity.badRequest().build();
            }
            if (date.isBefore(now.minusMonths(1))) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (from != null && to != null) {
            if (from.isAfter(to)) {
                return ResponseEntity.badRequest().build();
            }
        }

        if (date != null && from == null && to == null) {
            List<SingleSensorDataDto> dtoList = sensorDataService.getSingleSensorDataForDeviceDay(device, date, sensor);
            return ResponseEntity.ok().body(dtoList);
        }

        if (date != null && from != null && to != null) {
            List<SingleSensorDataDto> dtoList = sensorDataService.getSingleSensorDataForDeviceWithInterval(device, sensor, from, to);
            return ResponseEntity.ok().body(dtoList);
        }
        return ResponseEntity.notFound().build();
    }

}
