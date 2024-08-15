package com.hivebuddyteam.hivebuddyapplication.dto;

import com.hivebuddyteam.hivebuddyapplication.domain.User;

public class AddNewDeviceDto {

    private User user;

    private String deviceName;

    private String serialNumber;

    private String securityCode;

    public AddNewDeviceDto() {
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

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
