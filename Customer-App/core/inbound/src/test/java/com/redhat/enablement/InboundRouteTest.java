package com.redhat.enablement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:bundle-context.xml")
public class InboundRouteTest extends CamelSpringTestSupport {

    private static final Logger LOG = LoggerFactory
	    .getLogger(InboundRouteTest.class);

    @Autowired
    protected CamelContext camelContext;

    @Produce(uri = "direct:start")
    protected ProducerTemplate template;

    @Test
    public void shouldPostAPaylodToUri() throws IOException,
	    InterruptedException {
	createApplicationContext();
	// Given a file
	String file = readFile("src/test/resources/PatientDemographics.xml");
	StringEntity stringEntity = new StringEntity(file);

	HttpClient client;
	client = HttpClientBuilder.create().build();
	stringEntity.setContentType("application/xml");
	HttpPost httpPost = new HttpPost(
		"http://localhost:8181/cxf/test/deim/add");
	httpPost.setEntity(stringEntity);
	HttpResponse response = client.execute(httpPost);

	Thread.sleep(2000);

	// Then I should have an extra person
	assertNotNull(response);
	assertTrue(response.toString().contains("200"));
	HttpEntity entity = response.getEntity();
	assertNotNull(entity);
    }

    @Test
    public void shouldPostAnInvalidPaylodToUriAndReceiveBadResponse()
	    throws IOException, InterruptedException {
	createApplicationContext();
	// Given a file
	String file = readFile("src/test/resources/InvalidPatientDemographics.xml");
	StringEntity stringEntity = new StringEntity(file);

	HttpClient client;
	client = HttpClientBuilder.create().build();
	stringEntity.setContentType("application/xml");
	HttpPost httpPost = new HttpPost(
		"http://localhost:8181/cxf/test/deim/add");
	httpPost.setEntity(stringEntity);
	HttpResponse response = client.execute(httpPost);

	Thread.sleep(2000);

	// Then I should have an extra person
	assertNotNull(response);
	assertFalse(response.toString().contains("200"));
	HttpEntity entity = response.getEntity();
	assertNotNull(entity);
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
