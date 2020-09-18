package com.servicehub.WeatherServiceAdapter.model;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;

public class ServiceBrequest {
    private String txt;
    private String createdDt;
    private int currentTemp;

    public ServiceBrequest(){}

    public String getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(String createdDt) {
        this.createdDt = createdDt;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceBrequest)) return false;
        ServiceBrequest that = (ServiceBrequest) o;
        return getCurrentTemp() == that.getCurrentTemp() &&
                Objects.equals(getTxt(), that.getTxt()) &&
                Objects.equals(getCreatedDt(), that.getCreatedDt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTxt(), getCreatedDt(), getCurrentTemp());
    }
}
