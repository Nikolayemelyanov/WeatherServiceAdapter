package com.servicehub.WeatherServiceAdapter.model;

import java.util.Objects;

public class Temperature {
    int value;
    public Temperature(int value) {
        this.value = value;
    }
    public Temperature(){}

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temperature)) return false;
        Temperature that = (Temperature) o;
        return getValue() == that.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
