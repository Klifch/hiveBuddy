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

    private Boolean active;

    private String securityCode;

    public Device() {
    }

    public Device(User user, String deviceName, String serialNumber) {
        this.user = user;
        this.deviceName = deviceName;
        this.serialNumber = serialNumber;
    }

    public Device(User user, String deviceName, String serialNumber, Boolean active, String securityCode) {
        this.user = user;
        this.deviceName = deviceName;
        this.serialNumber = serialNumber;
        this.active = active;
        this.securityCode = securityCode;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
