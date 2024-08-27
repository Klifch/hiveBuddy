package com.hivebuddyteam.hivebuddyapplication.webapi;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.Notification;
import com.hivebuddyteam.hivebuddyapplication.domain.UnregisterdPoolUnit;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.NotificationService;
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
    private final NotificationService notificationService;

    public DeviceRestController(DeviceService deviceService, SensorDataService sensorDataService, UnregisteredPoolService unregisteredPoolService, NotificationService notificationService) {
        this.deviceService = deviceService;
        this.sensorDataService = sensorDataService;
        this.unregisteredPoolService = unregisteredPoolService;
        this.notificationService = notificationService;
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

        if (device.getSerialNumber().equals(serial) && device.getSecurityCode().equals(securityCode)) {
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

    @GetMapping("/getNotifications/{serial}")
    public ResponseEntity<List<Notification>> getNotifications(@PathVariable("serial") String serial) {

        Device device = deviceService.findBySerial(serial);
        if (device == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Notification> notifications = notificationService.getAllUncheckedForDevice(device);

        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/checkNotification/{id}")
    public ResponseEntity<Boolean> checkNotification(
            @PathVariable("id") Integer id
    ) {
        notificationService.checkNotification(id);

        return ResponseEntity.ok(true);
    }
}
