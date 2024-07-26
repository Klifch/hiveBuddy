package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;

import java.util.List;

public interface SensorDataService {

    List<SensorData> getAll();

    SensorData save(SensorData sensorData);

    SensorData save(SensorDataDto sensorDataDto);

    SensorData findLatestByDevice(Device device);

}
