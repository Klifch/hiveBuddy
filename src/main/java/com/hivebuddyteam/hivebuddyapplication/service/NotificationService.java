package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    List<Notification> getAllForDevice(Device device);

    List<Notification> getAllUncheckedForDevice(Device device);

    void checkNotification(Integer notificationId);

    void registerSensorNotification(Device device, String sensorType, Double value);

    Notification getLatestForDeviceBySensorType(Device device, String sensorType);
}
