package me.stronglift.api.filter;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import me.stronglift.api.controller.BaseController;
import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceMapper;
import me.stronglift.api.entity.User;
import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.util.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Auth filter for Jersey that filters every request coming into the API.
 * 
 * @author Dušan Eremić TODO - complete JavaDoc
 *
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter, ContainerResponseFilter {
	
	private static final String AUTHORIZATION = "Authorization";
	
	private static final String BASIC = "Basic";
	
	@Context
	protected ServiceFactory serviceFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
	
	/**
	 * Filter method called after a response has been provided for a request (either by a request filter or by a matched resource method).
	 */
	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		// Enabled Cross-origin resource sharing (CORS)
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
		responseContext.getHeaders().add("Access-Control-Allow-Headers", "Authorization, Content-Type, x-api-version");
	}
	
	/**
	 * Filter method called before a request has been dispatched to a resource.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		if ((HttpMethod.POST.equals(requestContext.getMethod()) || HttpMethod.OPTIONS.equals(requestContext.getMethod()))
				&& Resource.trimPath(requestContext.getUriInfo().getPath()).equals(Resource.trimPath(ResourceMapper.Path.USER))) {
			return;
		}
		
		String[] usernamePassword = decodeAuthorization(requestContext.getHeaderString(AUTHORIZATION));
		
		if (usernamePassword == null) {
			abortRequest(requestContext);
		}
		
		final User user = serviceFactory.getUserService().checkUser(usernamePassword[0], usernamePassword[1]);
		
		if (user == null) {
			abortRequest(requestContext);
		} else {
			requestContext.setProperty(BaseController.USER, user);
		}
		
	}
	
	private static String[] decodeAuthorization(String authorization) {
		if (authorization == null || !authorization.startsWith(BASIC)) {
			return null;
		}
		
		// Authorization: Basic base64credentials
		String base64Credentials = authorization.substring(BASIC.length()).trim();
		if (base64Credentials.isEmpty()) {
			return null;
		}
		
		// credentials = username:password
		String credentials = new String(java.util.Base64.getDecoder().decode(base64Credentials));
		
		// credentials split
		final String[] usernamePassword = credentials.split(":", 2);
		
		if (usernamePassword.length != 2) {
			return null;
		}
		
		if (usernamePassword[0].trim().isEmpty()) {
			return null;
		}
		
		if (usernamePassword[1].trim().isEmpty()) {
			return null;
		}
		
		return usernamePassword;
	}
	
	private void abortRequest(ContainerRequestContext requestContext) {
		requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
	}
}
