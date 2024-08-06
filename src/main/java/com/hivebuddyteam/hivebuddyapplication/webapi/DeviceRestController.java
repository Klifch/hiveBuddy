package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deviceapi")
public class DeviceRestController {

    private final DeviceService deviceService;
    private final SensorDataService sensorDataService;

    public DeviceRestController(DeviceService deviceService, SensorDataService sensorDataService) {
        this.deviceService = deviceService;
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/show")
    public List<Device> showData() {
        return deviceService.getAll();
    }

    @GetMapping("/status/{serial}")
    public ResponseEntity<Boolean> deviceStatus(@PathVariable("serial") String serial) {

        Device device = deviceService.findBySerial(serial);
        if (device == null) {
            return ResponseEntity.badRequest().build();
        }

        Boolean online = sensorDataService.checkIfAlive(device);

        return ResponseEntity.ok(online);
    }
}
