package com.servicehub.WeatherServiceAdapter.service;

import com.servicehub.WeatherServiceAdapter.model.Coordinates;
import com.servicehub.WeatherServiceAdapter.model.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherServiceAPI {
    @Value("${weather-service.api.path.weatherServiceAPI}")
    String serviceAPI;

    @Autowired
    RestTemplate restTemplate;

    public Temperature getTemperature(Coordinates coordinates) {
        String uri = serviceAPI + "?latitude=" + coordinates.getLatitude() + "&longitude=" + coordinates.getLongitude();
        Temperature temperature = restTemplate.getForObject(uri, Temperature.class);
        return temperature;
    }
}
