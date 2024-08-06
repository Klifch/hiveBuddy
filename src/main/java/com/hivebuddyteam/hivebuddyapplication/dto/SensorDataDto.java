package com.hivebuddyteam.hivebuddyapplication.dto;

import com.hivebuddyteam.hivebuddyapplication.domain.SensorData;

import java.math.BigDecimal;

public class SensorDataDto {

    private String serialNumber;

    private BigDecimal sensor1;

    private BigDecimal sensor2;

    private BigDecimal sensor3;

    private BigDecimal sensor4;

    private BigDecimal sensor5;

    public SensorDataDto() {
    }

    public SensorDataDto(String serialNumber, BigDecimal sensor1, BigDecimal sensor2, BigDecimal sensor3, BigDecimal sensor4, BigDecimal sensor5) {
        this.serialNumber = serialNumber;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
    }

    public static SensorDataDto mapToDto(String serialNumber, SensorData sensorData) {
        if (sensorData == null) {
            return new SensorDataDto(
                    serialNumber,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }

        return new SensorDataDto(
                serialNumber,
                sensorData.getSensor1() == null ? BigDecimal.ZERO : sensorData.getSensor1(),
                sensorData.getSensor2() == null ? BigDecimal.ZERO : sensorData.getSensor2(),
                sensorData.getSensor3() == null ? BigDecimal.ZERO : sensorData.getSensor3(),
                sensorData.getSensor4() == null ? BigDecimal.ZERO : sensorData.getSensor4(),
                sensorData.getSensor5() == null ? BigDecimal.ZERO : sensorData.getSensor5()
        );
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BigDecimal getSensor1() {
        return sensor1;
    }

    public void setSensor1(BigDecimal sensor1) {
        this.sensor1 = sensor1;
    }

    public BigDecimal getSensor2() {
        return sensor2;
    }

    public void setSensor2(BigDecimal sensor2) {
        this.sensor2 = sensor2;
    }

    public BigDecimal getSensor3() {
        return sensor3;
    }

    public void setSensor3(BigDecimal sensor3) {
        this.sensor3 = sensor3;
    }

    public BigDecimal getSensor4() {
        return sensor4;
    }

    public void setSensor4(BigDecimal sensor4) {
        this.sensor4 = sensor4;
    }

    public BigDecimal getSensor5() {
        return sensor5;
    }

    public void setSensor5(BigDecimal sensor5) {
        this.sensor5 = sensor5;
    }
}
