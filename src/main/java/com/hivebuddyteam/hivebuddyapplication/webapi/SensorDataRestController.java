package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensorapi")
public class SensorDataRestController {

    private final SensorDataService sensorDataService;

    public SensorDataRestController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
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

}
