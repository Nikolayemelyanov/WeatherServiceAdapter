package com.servicehub.WeatherServiceAdapter;

import com.servicehub.WeatherServiceAdapter.model.Coordinates;
import com.servicehub.WeatherServiceAdapter.model.Lng;
import com.servicehub.WeatherServiceAdapter.model.ServiceArequest;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Service;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@RunWith(CamelSpringBootRunner.class)
@MockEndpoints("direct:remoteWeatherServiceEnrichment")
public class ServiceAFilterMessageRouteTest {

    @EndpointInject("mock:direct:remoteWeatherServiceEnrichment")
    MockEndpoint mockEndpoint;

    @Autowired
    ProducerTemplate template;

    @Test
    @DirtiesContext
    public void testFilterMatchingObjectMethod() throws InterruptedException {
        Coordinates coordinates =  new Coordinates();
        coordinates.setLatitude("54.35");
        coordinates.setLongitude("52.52");
        ServiceArequest arequest =  new ServiceArequest();
        arequest.setLng(Lng.RU);
        arequest.setMsg("Привет");
        arequest.setCoordinates(coordinates);
        mockEndpoint.expectedBodiesReceived(arequest);
        mockEndpoint.expectedHeaderReceived("latitude","54.35");
        mockEndpoint.expectedHeaderReceived("longitude","52.52");
        template.sendBody("direct:validatedInput", arequest);
        mockEndpoint.assertIsSatisfied();

    }
    @Test
    public void testFilterNotMatchingObjectMethod() throws InterruptedException {
        Coordinates coordinates =  new Coordinates();
        ServiceArequest arequest =  new ServiceArequest();
        arequest.setLng(Lng.EN);
        arequest.setMsg("Привет");
        arequest.setCoordinates(coordinates);
        mockEndpoint.expectedBodiesReceived(arequest);
        template.sendBody("direct:validatedInput", arequest);
        mockEndpoint.assertIsNotSatisfied(1000);
    }


}
