package com.servicehub.WeatherServiceAdapter.Routes;
import com.servicehub.WeatherServiceAdapter.model.Lng;
import com.servicehub.WeatherServiceAdapter.model.ServiceArequest;
import com.servicehub.WeatherServiceAdapter.model.ServiceBrequest;
import com.servicehub.WeatherServiceAdapter.model.Temperature;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.http.NoHttpResponseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class WeatherAdapterRoute extends RouteBuilder {

    @Value("${weather-service.api.path.adapterServiceA}")
    String serviceA;

    @Value("${weather-service.api.path.weatherServiceAPI}")
    String weatherService;

    @Value("${weather-service.api.path.serviceB}")
    String serviceB;

    @Value("${server.port}")
    String port;

    @Value("${server.host}")
    String host;

    @Override
    public void configure() throws Exception {
        restConfiguration().component("servlet")
                .host(host)
                .port(port)
                .bindingMode(RestBindingMode.auto);


        onException(BeanValidationException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant(null);

        onException(NoHttpResponseException.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(444))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
                .setBody().constant(null);



        rest()
                .description("ServiceA")
                .id("ServiceA-route")
                .post(serviceA)
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .type(ServiceArequest.class)
                .enableCORS(true)
                .route()
                .to("bean-validator:serviceAvalidator")
                .to("direct:validatedInput");

        from("direct:validatedInput")
                .filter(new Predicate() {
                    @Override
                    public boolean matches(Exchange exchange) {
                        ServiceArequest bodyIn = exchange.getMessage().getBody(ServiceArequest.class);
                        Lng lng = bodyIn.getLng();
                        return lng.equals(Lng.RU);
                    }
                })
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        ServiceArequest bodyIn = exchange.getMessage().getBody(ServiceArequest.class);
                        Message message = exchange.getMessage();
                        message.setHeader("latitude", constant(bodyIn.getCoordinates().getLatitude()));
                        message.setHeader("longitude", constant(bodyIn.getCoordinates().getLongitude()));
                        exchange.setMessage(message);
                    }
                })
                .to("direct:remoteWeatherServiceEnrichment");

        from("direct:remoteWeatherServiceEnrichment")
                .routeId("TemperatureService")
                .enrich().simple(weatherService + "?latitude=${header.latitude}&longitude=${header.longitude}")
                .aggregationStrategy(
                        new AggregationStrategy() {
                    @Override
                    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
                        ServiceArequest bodyIn = oldExchange.getMessage().getBody(ServiceArequest.class);
                        Temperature temperature = newExchange.getMessage().getBody(Temperature.class);
                        ServiceBrequest serviceBmessage = new ServiceBrequest();
                        serviceBmessage.setCreatedDt(ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
                        serviceBmessage.setTxt(bodyIn.getMsg());
                        serviceBmessage.setCurrentTemp(temperature.getValue());
                        newExchange.getMessage().setBody(serviceBmessage);
                        return newExchange;
                    }
                })
                .to("direct:ServiceBRequest");

        from("direct:ServiceBRequest")
                .routeId("ServiceBpost")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .to(serviceB);
    }
}
