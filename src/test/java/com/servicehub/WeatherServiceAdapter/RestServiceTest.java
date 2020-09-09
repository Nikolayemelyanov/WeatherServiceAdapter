package com.servicehub.WeatherServiceAdapter;

import com.servicehub.WeatherServiceAdapter.service.GismeteoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class) //SpringRunner is an alias for the SpringJUnit4ClassRunner
@SpringBootTest
public class RestServiceTest {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GismeteoService service;

    private MockRestServiceServer mockServer;

    @Before
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetTemperature() {
        mockServer.expect(requestTo("https://www.gismeteo.ru/api/")).andRespond(withSuccess("{temperature : 10}", MediaType.APPLICATION_JSON));

        String result = service.getTemperature();


        mockServer.verify();
        assertEquals("{temperature : 10}", result);
    }
}
