package com.servicehub.WeatherServiceAdapter.service;

import com.servicehub.WeatherServiceAdapter.model.Coordinates;
import com.servicehub.WeatherServiceAdapter.model.ServiceBrequest;
import com.servicehub.WeatherServiceAdapter.model.Temperature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class ServiceBservice {
    @Value("${weather-service.api.path.serviceB}")
    String serviceBpath;

    RestTemplate restTemplate;

    public ServiceBservice(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder.build();
    }

    public ServiceBrequest postMessageServiceB(ServiceBrequest brequest) {
        //String uri = serviceBpath + "?latitude=" + coordinates.getLatitude() + "&longitude=" + coordinates.getLongitude();
        HttpEntity<ServiceBrequest> request = new HttpEntity<>(brequest);
        ResponseEntity<ServiceBrequest> response =
                restTemplate.exchange(serviceBpath, HttpMethod.POST, request, ServiceBrequest.class);
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        ServiceBrequest temperature = restTemplate.postForObject(serviceBpath, brequest, ServiceBrequest.class);
        return temperature;
    }
}
