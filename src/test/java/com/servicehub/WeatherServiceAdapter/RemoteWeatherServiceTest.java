package com.servicehub.WeatherServiceAdapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicehub.WeatherServiceAdapter.model.*;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest
@MockEndpoints
@RunWith(CamelSpringBootRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RemoteWeatherServiceTest extends CamelTestSupport  {

    @EndpointInject("mock:direct:ServiceBRequest")
    MockEndpoint mockEndpoint;

    @EndpointInject("mock:https://www.gismeteo.ru/api?latitude=54.35&longitude=52.52")
    MockEndpoint httpMockEndpoint;

    @Autowired
    ProducerTemplate template;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Value("${weather-service.api.path.mockWeatherServiceAPI}")
    String serviceAPI;

    @Override
    public String isMockEndpointsAndSkip() {
        return "https://www.gismeteo.ru/api*";
    }

    @Before
    public void setUp(){}


    @Test
    @DirtiesContext
    public void testWeatherServiceAPI() throws InterruptedException, URISyntaxException, JsonProcessingException {

        Coordinates coordinates =  new Coordinates();
        coordinates.setLatitude("54.35");
        coordinates.setLongitude("52.52");
        ServiceArequest arequest =  new ServiceArequest();
        arequest.setLng(Lng.RU);
        arequest.setMsg("Привет");
        arequest.setCoordinates(coordinates);
        ServiceBrequest serviceBmessage = new ServiceBrequest();
        serviceBmessage.setCreatedDt(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
        serviceBmessage.setTxt("Привет");
        serviceBmessage.setCurrentTemp(10);
        Map<String, Object> headers = new HashMap<>();
        headers.put("latitude", "54.35");
        headers.put("longitude", "52.52");
        template.sendBodyAndHeaders("direct:remoteWeatherServiceEnrichment", arequest, headers);
        httpMockEndpoint.whenAnyExchangeReceived(new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                exchange.getMessage().setBody("{value:10}", String.class);
            }
        });
        mockEndpoint.expectedBodiesReceived(serviceBmessage);
        mockEndpoint.assertIsSatisfied();
    }
}
