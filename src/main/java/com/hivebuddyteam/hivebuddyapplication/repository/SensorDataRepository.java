package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Integer> {

    SensorData findTopByDeviceOrderByTimestampDesc(Device device);

    List<SensorData> findAllByDeviceAndTimestampBetween(Device device, LocalDateTime start, LocalDateTime end);

    // magic inside this class ...

}
