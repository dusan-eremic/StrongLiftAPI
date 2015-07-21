package me.stronglift.api.app;

import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.service.ServiceFactory.FactoryImpl;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * Jersey application configuration.
 * 
 * @author Dusan Eremic
 *
 */
public class ApiApplication extends ResourceConfig {
	
	/**
	 * Default constructor koji koristi IN_MEMORY service factory
	 */
	public ApiApplication() {
		this(ServiceFactory.get(FactoryImpl.IN_MEMORY));
	}
	
	/**
	 * Constructor koji prihvata instancu service factory-ja
	 * 
	 * @param serviceFactory Service factory koje će biti korišćen za dependency
	 *            injection na svim mestima gde se traži ServiceFactory.class
	 */
	public ApiApplication(final ServiceFactory serviceFactory) {
		
		// Registracija paketa koji će biti skenirani za @Provider anotaciju
		packages("me.stronglift.api.controller");
		packages("me.stronglift.api.filter");
		packages("me.stronglift.api.error");
		packages("me.stronglift.api.model");
		packages("me.stronglift.api.controller.resource");
		
		// Registracija prosleđenog service factory-ja koji
		// će biti korišćen za dependency injection
		register(new AbstractBinder() {
			
			@Override
			protected void configure() {
				bind(serviceFactory).to(ServiceFactory.class);
			}
			
		});
		
		// Zamena defaultnog Json parsera za Jackson
		final JacksonJsonProvider json = new JacksonJsonProvider();
		json.configure(SerializationFeature.INDENT_OUTPUT, true);
		register(json);
		
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
	}
	
}
