package com.servicehub.WeatherServiceAdapter.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Lng {
    EN("en"),
    RU("ru"),
    ES("es");
    String language;
    Lng(String language) {
        this.language = language;
    }
    @JsonValue
    public String getLanguage() {
        return language;
    }
}
