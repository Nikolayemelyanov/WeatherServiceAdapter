package com.servicehub.WeatherServiceAdapter;

import java.time.LocalDateTime;

public class ServiceBmessage {
    private String msg;
    private LocalDateTime dateTime;
    private String temperature;

    ServiceBmessage(){}

    public String getTemperature() {
        return temperature;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
