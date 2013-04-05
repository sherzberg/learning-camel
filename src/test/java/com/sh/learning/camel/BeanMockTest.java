package com.sh.learning.camel;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;
import org.apache.camel.util.jndi.JndiContext;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.Context;

import static org.mockito.Mockito.mock;

@Ignore
public class BeanMockTest extends CamelTestSupport {

    @EndpointInject(uri = "mock:default")
    private MockEndpoint mockDefaultEndpoint;

    private SuperBean mockedSuperBean;

    @Test
    public void test_filterWithBean() throws Exception {
        mockedSuperBean = mock(SuperBean.class);

        String expectedBody = "<message attr='myattr' />";
        mockDefaultEndpoint.expectedBodiesReceived(expectedBody);

//        RouteDefinition routeDefinition = context.getRouteDefinition("myRouteId");
//        routeDefinition.adviceWith(context, new AdviceWithRouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                weaveById("beanMethodId")
//                        .replace()
//                        .bean(mockedSuperBean, "stuff").to(mockDefaultEndpoint);
//            }
//        });

        template.sendBody("direct:start", expectedBody);
        mockDefaultEndpoint.assertIsSatisfied();

//        verify(mockedSuperBean).stuff(any(String.class));
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                SuperBean superBean = (SuperBean) context.getRegistry().lookup("superBean");
                superBean.stuff(null);

                from("direct:start").routeId("myRouteId")
                        .beanRef("superBean", "stuff").id("beanMethodId")
                        .to("mock:default")
                        .end();
            }
        };
    }

    @Override
    protected Context createJndiContext() throws Exception {
        JndiContext context = new JndiContext();
        context.bind("superBean", new SuperBean());
        return context;
    }

    public static class SuperBean {
        public String stuff(String exchange) {
            System.out.printf("SuperBean.stuff(exchange = %s)", exchange);
            return exchange;
        }
    }

}
