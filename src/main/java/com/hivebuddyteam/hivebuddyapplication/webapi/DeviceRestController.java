package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.UnregisterdPoolUnit;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import com.hivebuddyteam.hivebuddyapplication.service.UnregisteredPoolService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/deviceapi")
public class DeviceRestController {

    private final DeviceService deviceService;
    private final SensorDataService sensorDataService;
    private final UnregisteredPoolService unregisteredPoolService;

    public DeviceRestController(DeviceService deviceService, SensorDataService sensorDataService, UnregisteredPoolService unregisteredPoolService) {
        this.deviceService = deviceService;
        this.sensorDataService = sensorDataService;
        this.unregisteredPoolService = unregisteredPoolService;
    }

    @GetMapping("/show")
    public List<Device> showData() {
        return deviceService.getAll();
    }

    @PostMapping("/auth")
    public ResponseEntity<String> registerDevice(@RequestBody UnregisterdPoolUnit unregisterdPoolUnit) {

        String serial = unregisterdPoolUnit.getSerialNumber();
        String securityCode = unregisterdPoolUnit.getSecurityCode();

        Device device = deviceService.findBySerial(serial);

        if (device == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (device.getSerialNumber().equals(serial) && device.getSecurityCode().equals(securityCode)){
            if (device.getActive()) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }

            if (!device.getActive() && unregisteredPoolService.checkIfSecurityCodeValid(securityCode, serial)) {
                device.setActive(true);
                deviceService.save(device);

                unregisteredPoolService.removeUnit(unregisterdPoolUnit);
                return ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
