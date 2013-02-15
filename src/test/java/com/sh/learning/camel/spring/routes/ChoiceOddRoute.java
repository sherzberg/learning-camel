package com.sh.learning.camel.spring.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ChoiceOddRoute extends SpringRouteBuilder {

    public static final String ENDPOINT_START = "direct:choice-odd-start";
    public static final String ROUTE_ID = "choiceOddRoute";
    public static final String ENDPOINT_END = "mock:choice-odd-end";

    @Override
    public void configure() throws Exception {
        from(ENDPOINT_START).routeId(ROUTE_ID)
                .to(ENDPOINT_END);
    }

}
