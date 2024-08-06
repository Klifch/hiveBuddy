package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.dto.SingleSensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/sensorapi")
public class SensorDataRestController {

    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;

    public SensorDataRestController(SensorDataService sensorDataService, DeviceService deviceService) {
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
    }

    @GetMapping("/sensors")
    public List<SensorData> showData() {
        return sensorDataService.getAll();
    }

    @PostMapping("/sensors")
    public SensorData saveData(@RequestBody SensorDataDto sensorDataDto) {

        SensorData newSensorData = sensorDataService.save(sensorDataDto);

        return newSensorData;
    }

    @GetMapping("/data/sensor/{serial}")
    public ResponseEntity<List<SingleSensorDataDto>> getSensorData(
            @PathVariable String serial,
            @RequestParam("minutes") Integer minutes,
            @RequestParam("sensorNumber") Integer sensorNumber,
            @RequestParam(value = "average", required = false) Boolean average,
            @RequestParam(value = "frequency", required = false) Integer frequency
    ) {

        if (sensorNumber > 5) {
            return ResponseEntity.badRequest().build();
        }

        Device device = deviceService.findBySerial(serial);

        if (device == null) {
            return ResponseEntity.badRequest().build();
        }

        if (average != null && average && frequency != null) {
            List<SingleSensorDataDto> dtoList = sensorDataService.getSingleSensorDataWithFrequency(device, sensorNumber, minutes, frequency);

            if (dtoList == null || dtoList.isEmpty()) {
                return ResponseEntity.ok().body(null);
            }

            return ResponseEntity.ok().body(dtoList);
        } else {
            return null;
        }
    }

}
