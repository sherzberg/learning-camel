package com.sh.learning.camel.spring;

import com.sh.learning.camel.spring.routes.SimpleRoute;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SimpleSpringJavaConfigScan {

    @Produce(uri = SimpleRoute.ENDPOINT_START)
    private ProducerTemplate template;

    @EndpointInject(uri = SimpleRoute.ENDPOINT_END)
    private MockEndpoint resultEndpoint;

    @Test
    public void test_route() throws Exception {
        String expectedBody = "<matched/>";

        resultEndpoint.expectedBodiesReceived(expectedBody);

        template.sendBodyAndHeader(expectedBody, "attr", "goodAttr");

        resultEndpoint.assertIsSatisfied();
    }

}
