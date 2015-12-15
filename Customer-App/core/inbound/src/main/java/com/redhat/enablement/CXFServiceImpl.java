package com.redhat.enablement;


import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.camel.Body;

import com.customer.app.Person;

@Path ("/deim")
public class CXFServiceImpl {
	
	@POST
	@Path("/add")
	public Person addPerson(@Body Person person) {
		System.out.println("hello");
		return person;
	}

}
