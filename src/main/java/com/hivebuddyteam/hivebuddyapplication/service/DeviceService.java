package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.User;

import java.util.List;

public interface DeviceService {

    List<Device> getAll();

    Device findBySerial(String serial);

    Device findById(Integer id);

    List<Device> findAllByUser(User user);

    Device save(Device device);

}
