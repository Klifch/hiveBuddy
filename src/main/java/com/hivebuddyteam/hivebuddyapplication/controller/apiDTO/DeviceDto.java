package com.hivebuddyteam.hivebuddyapplication.controller.apiDTO;

import com.hivebuddyteam.hivebuddyapplication.domain.User;

public class DeviceDto {

    private User user;

    private String deviceName;

    private String serialNumber;

    public DeviceDto() {
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
