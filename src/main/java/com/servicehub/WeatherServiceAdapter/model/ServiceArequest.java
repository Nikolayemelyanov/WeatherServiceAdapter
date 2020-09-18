package com.servicehub.WeatherServiceAdapter.model;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class ServiceArequest {
    private String msg;
    private Lng lng;
    private Coordinates coordinates;

    public ServiceArequest(){}

    @NotEmpty
    public String getMsg() {
        return msg;
    }

    public Lng getLng() {
        return lng;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setLng(Lng lng) {
        this.lng = lng;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceArequest)) return false;
        ServiceArequest arequest = (ServiceArequest) o;
        return getMsg().equals(arequest.getMsg()) &&
                getLng().equals(arequest.getLng()) &&
                getCoordinates().equals(arequest.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMsg(), getLng(), getCoordinates());
    }
}
