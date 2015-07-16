package me.stronglift.api.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging filter that logs every request and response.
 * 
 * @author Dušan Eremić
 *
 */
@Provider
@PreMatching
public class LoggingFilter implements ContainerRequestFilter,
		ContainerResponseFilter {

	/** A logger instance */
	private static final Logger logger = LoggerFactory
			.getLogger(LoggingFilter.class);

	/**
	 * Filter method called after a response has been provided for a request
	 * (either by a request filter or by a matched resource method).
	 */
	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		int httpStatus = responseContext.getStatus();
		Object entity = responseContext.getEntity();
		
		logger.debug("Response (status {}), Entity: {}", httpStatus, entity);
	}

	/**
	 * Filter method called before a request has been dispatched to a resource.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {

		logger.debug("Requesting {} method for path {}", requestContext
				.getMethod(), requestContext.getUriInfo().getRequestUri());
	}
}
