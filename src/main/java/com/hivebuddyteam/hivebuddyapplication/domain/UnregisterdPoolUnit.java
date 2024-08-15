package com.hivebuddyteam.hivebuddyapplication.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "unregistered_pool")
public class UnregisterdPoolUnit {

    @Id
    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "security_code", nullable = false)
    private String securityCode;

    public UnregisterdPoolUnit() {
    }

    public UnregisterdPoolUnit(String serialNumber, String securityCode) {
        this.serialNumber = serialNumber;
        this.securityCode = securityCode;
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
