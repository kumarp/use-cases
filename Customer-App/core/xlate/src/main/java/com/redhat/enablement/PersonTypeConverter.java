package com.redhat.enablement;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.support.TypeConverterSupport;

import com.sun.mdm.index.webservice.ExecuteMatchUpdate;

@Converter
public class PersonTypeConverter extends TypeConverterSupport {

    @Override
    public <T> T convertTo(Class<T> type, Exchange exchange, Object value)
	    throws TypeConversionException {
	//
	System.err.println("hello test");
	return (T) new ExecuteMatchUpdate();
    }

}
