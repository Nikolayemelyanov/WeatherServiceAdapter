package com.servicehub.WeatherServiceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.WeatherServiceAdapter.model.Coordinates;
import com.servicehub.WeatherServiceAdapter.model.Temperature;
import com.servicehub.WeatherServiceAdapter.service.WeatherServiceAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherServiceAPITest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private WeatherServiceAPI weatherService;


    private MockRestServiceServer mockServer;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather-service.api.path.weatherServiceAPI}")
    String serviceAPI;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testMockRestServiceServerGetTemperature() throws URISyntaxException, JsonProcessingException {
        Temperature temp =  new Temperature(10);
        Coordinates coordinates =  new Coordinates();
        coordinates.setLatitude("54.35");
        coordinates.setLongitude("52.52");
        String uri = serviceAPI + "?latitude=" + coordinates.getLatitude() + "&longitude=" + coordinates.getLongitude();
        mockServer.expect(ExpectedCount.once(), requestTo(new URI(uri)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsString(temp)));

        Temperature result = weatherService.getTemperature(coordinates);
        mockServer.verify();
        assertEquals(temp, result);
    }
}

