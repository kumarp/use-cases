package com.redhat.enablement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:/META-INF/spring/bundle-context.xml")
public class InboundRouteTest extends CamelTestSupport {
	
	private static final Logger LOG = LoggerFactory.getLogger(InboundRouteTest.class);
	
    @Autowired
    protected CamelContext camelContext;
    
    @Produce(uri= "cxf:bean:CXFServiceImpl")
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:validation")
    protected MockEndpoint resultEndpoint;
	
	@Test
	public void shouldPostAPaylod() throws IOException, InterruptedException {
		//Given a file
		String file = readFile("src/test/resources/PatientDemographics.xml");
		resultEndpoint.setExpectedMessageCount(1);
		//ersonObject expectedPerson = new PersonObject();
		//resultEndpoint.expectedBodiesReceived(expectedPerson);
		
		//When an endpoint is hit
		template.sendBody(file);
		
		//Then I should have an extra person
		resultEndpoint.assertIsSatisfied();
	}
	
	public String readFile ( String fileLocation ) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader(fileLocation) );
		StringBuilder builder = new StringBuilder();
		String ls = "\n";
		String line = null;
	    while( ( line = reader.readLine() ) != null ) {
	    	builder.append( line );
	    	builder.append( ls );
	    }
	    reader.close();
	    return builder.toString();
	}
}
