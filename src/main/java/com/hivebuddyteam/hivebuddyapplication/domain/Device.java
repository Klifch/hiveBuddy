package com.hivebuddyteam.hivebuddyapplication.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String deviceName;

    private String serialNumber;

    public Device() {
    }

    public Device(User user, String deviceName, String serialNumber) {
        this.user = user;
        this.deviceName = deviceName;
        this.serialNumber = serialNumber;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    // TODO: DELETE AFTER TESTING
    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}
