package com.redhat.enablement.implemenation;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.camel.CamelContext;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.customer.app.Person;
import com.redhat.enablement.CXFService;

@Path("/deim")
public class CXFServiceImpl implements CXFService {

    @Autowired
    protected CamelContext context;

    @Produce(uri = "direct:marshall")
    protected ProducerTemplate template;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.redhat.enablement.Implemenation.CXFService#addPerson(com.customer
     * .app.Person)
     */
    @Override
    @POST
    @Path("/add")
    public Response addPerson(Person person) {
	Response response = null;
	if (isValid(person)) {
	    template = (template != null) ? template : context
		    .createProducerTemplate();
	    template.sendBody(person);
	    response = Response.ok().build();
	} else {
	    response = Response.status(400).build();
	}

	return response;
    }

    private boolean isValid(Person person) {
	if (person.getLegalname() != null
		&& (person.getLegalname().getGiven() != null && person
			.getLegalname().getGiven() != "")
		&& (person.getLegalname().getFamily() != null && person
			.getLegalname().getFamily() != "")) {
	    return true;
	}
	return false;
    }

}
