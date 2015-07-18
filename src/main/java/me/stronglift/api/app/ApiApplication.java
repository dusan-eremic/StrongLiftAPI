package me.stronglift.api.app;

import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.service.ServiceFactory.FactoryImpl;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Jersey application setup.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public class ApiApplication extends ResourceConfig {
	
	public ApiApplication() {
		this(ServiceFactory.get(FactoryImpl.IN_MEMORY));
	}
	
	public ApiApplication(final ServiceFactory serviceFactory) {
		
		packages("me.stronglift.api.controller");
		packages("me.stronglift.api.filter");
		packages("me.stronglift.api.error");
		packages("me.stronglift.api.model");
		packages("me.stronglift.api.controller.resource");
		
		register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(serviceFactory).to(ServiceFactory.class);
			}
			
		});
		
		final JacksonJsonProvider json = new JacksonJsonProvider();
		json.configure(SerializationFeature.INDENT_OUTPUT, true);
		register(json);
		
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true); // Sends an error message inside the response body
	}
	
}
