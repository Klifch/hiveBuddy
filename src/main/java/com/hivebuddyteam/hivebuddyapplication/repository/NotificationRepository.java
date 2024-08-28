package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    List<Notification> getAllByDevice(Device device);

    List<Notification> getAllByDeviceAndCheckedIsFalse(Device device);

//    I use list here cuz when load on server is high stuttering can occur and cause creation of 2 notifications with same timestamp
    @Query("SELECT n FROM Notification n WHERE n.device = :device AND n.sensorType = :sensorType ORDER BY n.timestamp DESC")
    List<Notification> findLatestByDeviceAndSensorType(@Param("device") Device device, @Param("sensorType") String sensorType);
}
