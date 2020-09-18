package com.servicehub.WeatherServiceAdapter;

import com.servicehub.WeatherServiceAdapter.model.Coordinates;
import com.servicehub.WeatherServiceAdapter.model.Lng;
import com.servicehub.WeatherServiceAdapter.model.ServiceArequest;
import com.servicehub.WeatherServiceAdapter.model.ServiceBrequest;
import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockEndpoints("direct:validatedInput")
public class ServiceARequestPostRestTest {

    @Autowired
    TestRestTemplate restTemplate;

    @EndpointInject("mock:direct:validatedInput")
    MockEndpoint mockEndpoint;

    @Value("${weather-service.api.path.adapterServiceA}")
    String serviceA;

    @Test
    public void testPostValidRequest() throws InterruptedException {
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude("54.35");
        coordinates.setLongitude("52.52");
        ServiceArequest arequest = new ServiceArequest();
        arequest.setLng(Lng.RU);
        arequest.setMsg("Привет");
        arequest.setCoordinates(coordinates);
        mockEndpoint.expectedBodiesReceived(arequest);
        HttpEntity<ServiceArequest> request = new HttpEntity<>(arequest);
        ResponseEntity<ServiceArequest> response = restTemplate.postForEntity(serviceA, request, ServiceArequest.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testPostNotValidRequest() {
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude("54.35");
        coordinates.setLongitude("52.52");
        ServiceArequest arequest = new ServiceArequest();
        arequest.setLng(Lng.RU);
        arequest.setMsg("");
        arequest.setCoordinates(coordinates);
        HttpEntity<ServiceArequest> request = new HttpEntity<>(arequest);
        ResponseEntity<ServiceArequest> response = restTemplate.postForEntity(serviceA, request, ServiceArequest.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
