package com.sh.learning.camel;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.CamelTestSupport;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import javax.naming.Context;
import java.util.Random;

import static org.mockito.Mockito.mock;

@Ignore
public class BeanFilterTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:filtered")
    private MockEndpoint mockFilteredInput;

    @EndpointInject(uri = "mock:default")
    private MockEndpoint mockDefaultEndpoint;

    @Mock
    private SuperBean mockedSuperBean;

    @Before
    public void setup() {
        mockedSuperBean = mock(SuperBean.class);
    }

    @Test
    public void test_filterWithBean() throws Exception {
        String expectedBody = "<message attr='myattr' />";
        mockFilteredInput.expectedBodiesReceived(expectedBody);

        RouteDefinition routeDefinition = context.getRouteDefinition("myRouteId");
        routeDefinition.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("filterId")
                        .replace()
                        .filter().method("superBean", "matches");
            }
        });

        template.sendBody("direct:start", expectedBody);
        mockFilteredInput.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .routeId("myRouteId")
                        .filter()
                        .method("superBean", "matches").id("filterId").to("mock:filtered").end()
                        .to("mock:default");
            }
        };
    }

    @Override
    protected Context createJndiContext() throws Exception {
        Context context = super.createJndiContext();
        context.bind("superBean", new SuperBean());
        return context;
    }

    public static class SuperBean {
        public boolean matches(Exchange exchange) {
            return new Random().nextBoolean();
        }
    }

}
