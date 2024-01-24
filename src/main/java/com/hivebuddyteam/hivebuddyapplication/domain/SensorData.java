package com.hivebuddyteam.hivebuddyapplication.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer dataId;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    private LocalDateTime timestamp;

    private BigDecimal sensor1;

    private BigDecimal sensor2;

    private BigDecimal sensor3;

    private BigDecimal sensor4;

    private BigDecimal sensor5;

    public SensorData() {
    }

    public Integer getDataId() {
        return dataId;
    }

    public SensorData(Device device, LocalDateTime timestamp, BigDecimal sensor1, BigDecimal sensor2, BigDecimal sensor3, BigDecimal sensor4, BigDecimal sensor5) {
        this.device = device;
        this.timestamp = timestamp;
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
    }

    // TODO: DELETE AFTER TESTING
    public void setDataId(Integer dataId) {
        this.dataId = dataId;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
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
