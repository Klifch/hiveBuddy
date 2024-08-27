package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.Notification;
import com.hivebuddyteam.hivebuddyapplication.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getAllForDevice(Device device) {
        return notificationRepository.getAllByDevice(device);
    }

    @Override
    public List<Notification> getAllUncheckedForDevice(Device device) {
        return notificationRepository.getAllByDeviceAndCheckedIsFalse(device);
    }

    @Override
    public void checkNotification(Integer notificationId) {
        try {
            Notification notification = notificationRepository.getReferenceById(notificationId);
            notification.setChecked(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void registerSensorNotification(Device device, String sensorType, Double value) {
        Optional<Notification> latestNotification = notificationRepository.findLatestByDeviceAndSensorType(device, sensorType);
        if (latestNotification.isPresent()) {
            LocalDateTime offset = LocalDateTime.now().minusMinutes(5);

            if (latestNotification.get().getTimestamp().isAfter(offset)) {
                return;
            }
        }

        Notification notification;

        switch (sensorType) {
            case "temp":
                notification = new Notification(
                        device,
                        String.format(
                                "Device %s detected high temperature: %f degrees",
                                device.getSerialNumber(),
                                value
                        ),
                        sensorType,
                        LocalDateTime.now(),
                        false
                );
                notificationRepository.save(notification);
                break;

            case "humid":
                notification = new Notification(
                        device,
                        String.format(
                                "Device %s detected high humidity: %f",
                                device.getSerialNumber(),
                                value
                        ),
                        sensorType,
                        LocalDateTime.now(),
                        false
                );
                notificationRepository.save(notification);
                break;

            case "water":
                String level = switch (value.intValue()) {
                    case 1 -> "small leak";
                    case 2 -> "large leak";
                    case 3 -> "flood";
                    default -> throw new IllegalArgumentException("Invalid water sensor value: " + value);
                };

                notification = new Notification(
                        device,
                        String.format(
                                "Device %s: Seems like there is a %s in a hive!",
                                device.getSerialNumber(),
                                level
                        ),
                        sensorType,
                        LocalDateTime.now(),
                        false
                );
                notificationRepository.save(notification);
                break;

            default:
                throw new IllegalArgumentException("Invalid sensor type: " + sensorType);

        }
    }

    @Override
    public Optional<Notification> getLatestForDeviceBySensorType(Device device, String sensorType) {
        return notificationRepository.findLatestByDeviceAndSensorType(device, sensorType);
    }
}