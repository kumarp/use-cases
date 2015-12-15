package com.redhat.enablement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:/META-INF/spring/bundle-context.xml")
public class InboundRouteTest extends CamelSpringTestSupport {
	
	private static final Logger LOG = LoggerFactory.getLogger(InboundRouteTest.class);
	
    @Autowired
    protected CamelContext camelContext;
    
    @Produce(uri= "direct:start")
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:validation")
    protected MockEndpoint resultEndpoint;
	
	@Test
	public void shouldPostAPaylod() throws IOException, InterruptedException {
		createApplicationContext();
		//Given a file
		String file = readFile("src/test/resources/PatientDemographics.xml");
		resultEndpoint.setExpectedMessageCount(1);
		//ersonObject expectedPerson = new PersonObject();
		//resultEndpoint.expectedBodiesReceived(expectedPerson);
		
		//When an endpoint is hit
		template.sendBody(file);
		
		Thread.sleep(5000);
		//Then I should have an extra person
		resultEndpoint.assertIsSatisfied();
	}
	
	@Test
	public void shouldPostAPaylodToUri() throws IOException, InterruptedException {
		createApplicationContext();
		//Given a file
		String file = readFile("src/test/resources/PatientDemographics.xml");
		StringEntity stringEntity = new StringEntity(file);
		
		resultEndpoint.setExpectedMessageCount(1);
		
		HttpClient client;
		client = HttpClientBuilder.create().build();
		stringEntity.setContentType("application/xml");
		HttpPost httpPost = new HttpPost("http://localhost:9090/deim/add");
		httpPost.setEntity(stringEntity);
		HttpResponse response = client.execute( httpPost );
		
		Thread.sleep(5000);
		
		System.err.println(response);
		
		//Then I should have an extra person
		resultEndpoint.assertIsSatisfied();
		
		HttpEntity entity = response.getEntity();
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

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("/META-INF/spring/bundle-context.xml");
	}
}
