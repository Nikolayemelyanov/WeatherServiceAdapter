package com.servicehub.WeatherServiceAdapter;

public class ServiceAmessage {
    private String msg;
    private String lng;
    private String latitude;
    private String longitude;

    ServiceAmessage(){}

    public String getMsg() {
        return msg;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLng() {
        return lng;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
