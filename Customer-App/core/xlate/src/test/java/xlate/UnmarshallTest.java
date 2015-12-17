package xlate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:bundle-context.xml")
public class UnmarshallTest extends CamelSpringTestSupport {
    private static final Logger LOG = LoggerFactory
	    .getLogger(UnmarshallTest.class);

    @Autowired
    protected CamelContext camelContext;

    @Produce(uri = "q.empi.deim.in")
    protected ProducerTemplate template;

    @Test
    public void shouldConvertAPerson() throws IOException, InterruptedException {
	// createApplicationContext();
	// Given a valid file of type PatientDemographics
	String file = readFile("src/test/resources/PatientDemographics.xml");

	// When I send a message to the in queue
	template.setDefaultEndpointUri("activemq:queue:q.empi.deim.in");
	template.sendBody("activemq:queue:q.empi.deim.in", file);

	// Then I should get a message of the converted type
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
		"bundle-context.xml");
	return context;
    }

}
