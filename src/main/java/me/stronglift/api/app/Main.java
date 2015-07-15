package me.stronglift.api.app;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.service.ServiceFactory.FactoryImpl;

/**
 * Main class - API can be run from here as a regular Java app or deployed to Tomcat.
 * 
 * @author Dusan Eremic
 *
 */
public class Main {
	// Base URI the Grizzly HTTP server will listen on
	public static final String BASE_URI = "http://localhost:8080/";
	
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	
	/**
	 * Main method.
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
				
		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(//
				URI.create(BASE_URI), //
				new ApiApplication(ServiceFactory.get(FactoryImpl.IN_MEMORY))//
				);
		log.info("Jersey app started with WADL available at " + "{}application.wadl", BASE_URI);
		log.info("Hit enter to stop it...");
		System.in.read();
		server.shutdownNow();
	}
}
