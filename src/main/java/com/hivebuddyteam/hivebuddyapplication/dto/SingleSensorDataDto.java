package com.hivebuddyteam.hivebuddyapplication.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SingleSensorDataDto {

    private String serial;
    private LocalDateTime timestamp;
    private BigDecimal value;

    public SingleSensorDataDto(String serial, LocalDateTime timestamp, BigDecimal value) {
        this.serial = serial;
        this.timestamp = timestamp;
        this.value = value;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
