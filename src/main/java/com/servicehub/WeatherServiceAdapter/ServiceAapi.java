package com.servicehub.WeatherServiceAdapter;

import com.servicehub.WeatherServiceAdapter.service.GismeteoService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ServiceAapi extends RouteBuilder {


    GismeteoService gismeteoService;

    @Autowired
    ServiceAapi(GismeteoService gismeteoService) {
        this.gismeteoService = gismeteoService;
    }

    @Override
    public void configure() throws Exception {
        CamelContext context =  new DefaultCamelContext();
        restConfiguration().component("servlet")
                .bindingMode(RestBindingMode.json);
        rest("/api").description("ServiceApath")
                .id("ServiceA-route").post("/serviceA")
                .consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .bindingMode(RestBindingMode.auto)
                .type(ServiceAmessage.class)
                .enableCORS(true)
                .to("direct:remoteService");

        from("direct:remoteService").routeId("TemperatureService")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        ServiceAmessage bodyIn = (ServiceAmessage) exchange.getIn().getBody();
                        if (bodyIn.getLng().equals("ru")) {
                           String temperature = gismeteoService.getTemperature();
                           ServiceBmessage serviceBmessage = new ServiceBmessage();
                           serviceBmessage.setDateTime(LocalDateTime.now());
                           serviceBmessage.setMsg(bodyIn.getMsg());
                           serviceBmessage.setTemperature(temperature);
                           exchange.getIn().setBody(serviceBmessage);
                        }
                    }
                })
             //   .to("/serviceB")
        ;

    }
}
