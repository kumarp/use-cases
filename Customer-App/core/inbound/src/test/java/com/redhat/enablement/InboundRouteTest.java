package com.redhat.enablement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class InboundRouteTest {
	
	@Test
	public void shouldPostAPaylod() throws IOException {
		//Given a file
		String file = readFile("src/test/resources/PatientDemographics.xml");
		
		//When an endpoint is hit
		
		
		//Then I should have an exta person
		
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
