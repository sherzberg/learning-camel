package com.sh.learning.camel.spring.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends SpringRouteBuilder {

    public static final String ENDPOINT_START = "direct:start";
    public static final String ROUTE_ID = "myRoute";
    public static final String ENDPOINT_END = "mock:result";

    @Override
    public void configure() throws Exception {
        from(ENDPOINT_START).routeId(ROUTE_ID)
                .to(ENDPOINT_END);
    }

}
