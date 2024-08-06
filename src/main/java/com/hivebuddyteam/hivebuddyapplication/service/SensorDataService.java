package com.hivebuddyteam.hivebuddyapplication.service;

import com.hivebuddyteam.hivebuddyapplication.dto.SensorDataDto;
import com.hivebuddyteam.hivebuddyapplication.domain.Device;
import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;
import com.hivebuddyteam.hivebuddyapplication.dto.SingleSensorDataDto;

import java.util.List;

public interface SensorDataService {

    List<SensorData> getAll();

    SensorData save(SensorData sensorData);

    SensorData save(SensorDataDto sensorDataDto);

    SensorData findLatestByDevice(Device device);

    List<SingleSensorDataDto> getSingleSensorDataWithFrequency(Device device, Integer sensorNumber, Integer minutes, Integer frequency);

    List<SensorDataDto> getDeviceSensorDataWithFrequency(Device device, Integer minutes, Integer frequency);

    Boolean checkIfAlive(Device device);

}
