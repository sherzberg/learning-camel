package com.sh.learning.camel.spring;

import com.sh.learning.camel.spring.routes.ChoiceEvenOrOddRoute;
import com.sh.learning.camel.spring.routes.ChoiceEvenRoute;
import com.sh.learning.camel.spring.routes.ChoiceOddRoute;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringJavaConfigScanChoice {

    @Produce(uri = ChoiceEvenOrOddRoute.ENDPOINT_START)
    private ProducerTemplate startRoute;

    @EndpointInject(uri = ChoiceEvenRoute.ENDPOINT_START)
    private ProducerTemplate evenRoute;

    @EndpointInject(uri = ChoiceOddRoute.ENDPOINT_START)
    private ProducerTemplate oddRoute;

    @EndpointInject(uri = ChoiceEvenRoute.ENDPOINT_END)
    private MockEndpoint evenEndpoint;

    @EndpointInject(uri = ChoiceOddRoute.ENDPOINT_END)
    private MockEndpoint oddEndpoint;

    @Test
    @DirtiesContext
    public void test_route_even() throws Exception {
        String expectedBody = "<matched/>";

        evenEndpoint.expectedBodiesReceived(expectedBody);
        oddEndpoint.expectedMessageCount(0);

        startRoute.sendBodyAndHeader(expectedBody, "evenodd", "even");

        evenEndpoint.assertIsSatisfied();
        oddEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void test_route_odd() throws Exception {
        String expectedBody = "<matched/>";

        oddEndpoint.expectedBodiesReceived(expectedBody);
        evenEndpoint.setExpectedMessageCount(0);

        startRoute.sendBodyAndHeader(expectedBody, "evenodd", "odd");

        oddEndpoint.assertIsSatisfied();
        evenEndpoint.assertIsSatisfied();
    }

}
