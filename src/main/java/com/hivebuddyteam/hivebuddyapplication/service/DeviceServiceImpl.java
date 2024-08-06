package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.User;
import com.hivebuddyteam.hivebuddyapplication.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device findBySerial(String serial) {
        return deviceRepository.findDeviceBySerialNumber(serial);
    }

    @Override
    public List<Device> findAllByUser(User user) {
        return deviceRepository.findAllByUser(user);
    }

    @Override
    public Device findById(Integer id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

}
