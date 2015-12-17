package com.redhat.enablement;

import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.customer.app.Person;

@Path("/deim")
public interface CXFService {

	public abstract Response addPerson(Person person);

}