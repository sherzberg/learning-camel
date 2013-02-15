package com.sh.learning.camel.spring;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;

public class SimpleHeaderRouteFilterTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:result")
    private MockEndpoint resultEndpoint;

    @Produce(uri = "direct:start")
    private ProducerTemplate template;

    @Before
    public void setup() {
        template.setDefaultEndpoint(new MockEndpoint());
    }

    @Test
    public void test_filter_true() throws Exception {

        String expectedBody = "<matched/>";

        resultEndpoint.expectedBodiesReceived(expectedBody);

        template.sendBodyAndHeader(expectedBody, "attr", "goodAttr");

        resultEndpoint.assertIsSatisfied();
    }

    @Test
    public void test_filter_false() throws Exception {

        resultEndpoint.expectedMessageCount(0);

        template.sendBodyAndHeader("<nomatched/>", "attr", "badAttr");

        resultEndpoint.assertIsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .id("routeId")
                        .routeId("routeId")
                        .filter(header("attr").isEqualTo("goodAttr")).id("filterId")
                        .to("mock:result");
            }
        };
    }

}
