package com.redhat.enablement;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.camel.Body;

import com.customer.app.Person;

@Path ("/deim")
@WebService(serviceName = "CXFServiceImpl")
public class CXFServiceImpl {
	
	@GET
	public Person addPerson(@Body Person person) {
		System.out.println("hello");
		return person;
	}

}
