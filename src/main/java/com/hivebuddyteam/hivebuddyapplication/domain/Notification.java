package com.hivebuddyteam.hivebuddyapplication.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    private String message;

    @Column(name = "sensor_type")
    private String sensorType;

    private LocalDateTime timestamp;

    private Boolean checked;

    public Notification() {
    }

    public Notification(Device device, String message, String sensorType, LocalDateTime timestamp, Boolean checked) {
        this.device = device;
        this.message = message;
        this.sensorType = sensorType;
        this.timestamp = timestamp;
        this.checked = checked;
    }

    public Notification(Device device, String message, LocalDateTime timestamp, Boolean checked) {
        this.device = device;
        this.message = message;
        this.timestamp = timestamp;
        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }
}
