package me.stronglift.api.app;

import java.io.IOException;
import java.net.URI;

import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.service.ServiceFactory.FactoryImpl;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.UriBuilder;

/**
 * Main class - API može biti pokrenut kao standalone Java app ili možete biti
 * deployed na aplikacioni server kao što je Tomcat
 *
 * @author Dusan Eremic
 *
 */
public class Main {

	/**
	 * Base URL koji će biti korišćen u standalone modu
	 */
	public static final String BASE_URI = "http://localhost:8080/";

	/**
	 * Logger
	 */
	private static final Logger log = LoggerFactory.getLogger(Main.class);

	/**
	 * Main method.
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		final int port = System.getenv("PORT") != null ? Integer.valueOf(System.getenv("PORT")) : 8080;
    final URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();

		final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(//
				baseUri, //
				new ApiApplication(ServiceFactory.get(FactoryImpl.IN_MEMORY))//
				);

		Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
            server.shutdownNow();
        }
    });

		log.info("Jersey app started with WADL available at {}application.wadl", baseUri.toString());

		Thread.currentThread().join();
	}
}
