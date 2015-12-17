package xlate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Ignore
public class UnmarshallTest extends CamelSpringTestSupport {
    private static final Logger LOG = LoggerFactory
	    .getLogger(UnmarshallTest.class);

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:direct:converter")
    protected MockEndpoint resultEndpoint;

    @Test
    public void shouldUnmarshalAPerson() throws IOException,
	    InterruptedException {
	// createApplicationContext();
	// Given a file
	String file = readFile("src/test/resources/PatientDemographics.xml");
	// System.out.println(file);
	resultEndpoint.setExpectedMessageCount(1);

	// resultEndpoint.setExpectedMessageCount(1);

	// When an endpoint is hit
	template.sendBody(file);

	Thread.sleep(5000);
	// Then I should have an extra person
	resultEndpoint.assertIsSatisfied();

    }

    public String readFile(String fileLocation) throws IOException {
	BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
	StringBuilder builder = new StringBuilder();
	String ls = "\n";
	String line = null;
	while ((line = reader.readLine()) != null) {
	    builder.append(line);
	    builder.append(ls);
	}
	reader.close();
	return builder.toString();
    }

    @Override
    protected AbstractApplicationContext createApplicationContext() {
	ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
		"/META-INF/spring/bundle-context.xml");
	return context;
    }

}
