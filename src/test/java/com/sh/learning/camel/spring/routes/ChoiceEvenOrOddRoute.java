package com.sh.learning.camel.spring.routes;

import org.apache.camel.spring.SpringRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ChoiceEvenOrOddRoute extends SpringRouteBuilder {

    public static final String ENDPOINT_START = "direct:choice-evenodd-route";
    public static final String ROUTE_ID = "choiceEvenOddRoute";

    @Override
    public void configure() throws Exception {
        from(ENDPOINT_START).routeId(ROUTE_ID)
                .choice()
                    .when(header("evenodd").isEqualTo("even")).to(ChoiceEvenRoute.ENDPOINT_START)
                    .otherwise().to(ChoiceOddRoute.ENDPOINT_START);
    }

}
