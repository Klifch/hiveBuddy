package com.hivebuddyteam.hivebuddyapplication.controller;

import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.service.DeviceService;
import com.hivebuddyteam.hivebuddyapplication.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


import java.util.concurrent.ConcurrentHashMap;

@Controller
public class WebSocketController {

    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    private final SimpMessagingTemplate template;
    private final Map<String, ScheduledExecutorService> schedulers = new ConcurrentHashMap<>();

    @Autowired
    public WebSocketController(SensorDataService sensorDataService,
                               SimpMessagingTemplate template,
                               DeviceService deviceService
    ) {
        this.sensorDataService = sensorDataService;
        this.template = template;
        this.deviceService = deviceService;
    }

    @MessageMapping("/private")
    public void sendTestDataToUser(@Payload Map<String, String> payload) {
        System.out.println("Received payload: " + payload);

        String serialValue = payload.get("name");
        String status = payload.get("status");

        Device device = deviceService.findBySerial(serialValue);

        if (device == null) {
            throw new RuntimeException("Device is not in the session!");
        }

        if ("on".equalsIgnoreCase(status)) {
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            schedulers.put(device.getUser().getUsername(), scheduler);

            scheduler.scheduleAtFixedRate(() -> {
                SensorData sensorData = sensorDataService.findLatestByDevice(device);

                SensorDataDto sensorDataDto = new SensorDataDto();
                sensorDataDto.setSensor1(sensorData.getSensor1());
                sensorDataDto.setSensor2(sensorData.getSensor2());
                sensorDataDto.setSensor3(sensorData.getSensor3());
                sensorDataDto.setSensor4(sensorData.getSensor4());
                sensorDataDto.setSensor5(sensorData.getSensor5());

                System.out.println("INSIDE SCHEDULER -> SEND");
                template.convertAndSendToUser(device.getUser().getUsername(), "/specific", sensorDataDto);
            }, 0, 500, TimeUnit.MILLISECONDS);
        } else if ("off".equalsIgnoreCase(status)) {
            ScheduledExecutorService scheduler = schedulers.remove(device.getUser().getUsername());
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdownNow();
            }
        }
    }
}
