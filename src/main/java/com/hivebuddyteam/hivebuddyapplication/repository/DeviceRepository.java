package com.hivebuddyteam.hivebuddyapplication.repository;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    Device findDeviceBySerialNumber(String serial);

    List<Device> findAllByUser(User user);
}
