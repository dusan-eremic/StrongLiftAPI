package me.stronglift.api.filter;

import java.io.IOException;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import me.stronglift.api.controller.BaseController;
import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceMapper;
import me.stronglift.api.model.User;
import me.stronglift.api.service.ServiceFactory;
import me.stronglift.api.util.HttpMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jersey filter za svaki dolaće request.
 * 
 * Koristi se za Basic autentikaciju.
 * 
 * @author Dušan Eremić
 *
 */
@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter,
		ContainerResponseFilter {
	
	/** Auth header */
	private static final String AUTHORIZATION = "Authorization";
	
	/** Auth basic */
	private static final String BASIC = "Basic";
	
	/** Instanca service factory-ja */
	@Context
	private ServiceFactory serviceFactory;
	
	/** Logger */
	private static final Logger logger = LoggerFactory
			.getLogger(AuthFilter.class);
	
	/**
	 * Metoda dodaje Access-Control header-e u response i loguje response.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		// Enabled Cross-origin resource sharing (CORS)
		responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
		responseContext.getHeaders().add("Access-Control-Allow-Methods",
				"GET, POST, DELETE, PUT");
		responseContext.getHeaders().add("Access-Control-Allow-Headers",
				"Authorization, Content-Type, x-api-version");
		
		int httpStatus = responseContext.getStatus();
		Object entity = responseContext.getEntity();
		logger.debug("Response (status {}), Entity: {}", httpStatus, entity);
	}
	
	/**
	 * Metoda filtira SVE dolazeće requestove i vrši basic autentikaciju
	 * korisnika. Jedini request koji se ne autentifikuje je registracija novog
	 * korisnika.
	 */
	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		
		logger.debug("Requesting {} method for path {}", requestContext
				.getMethod(), requestContext.getUriInfo().getRequestUri());
		
		if ((HttpMethod.POST.equals(requestContext.getMethod()) || HttpMethod.OPTIONS
				.equals(requestContext.getMethod()))
				&& Resource.trimPath(requestContext.getUriInfo().getPath())
						.equals(Resource.trimPath(ResourceMapper.Path.USER))) {
			logger.debug("Registration request - no authorization is required.");
			return;
		}
		
		String[] usernamePassword = decodeAuthorization(requestContext
				.getHeaderString(AUTHORIZATION));
		
		if (usernamePassword == null) {
			throw new NotAuthorizedException(
					"Basic Authorization is missing or invalid.", BASIC);
		}
		
		final User user = serviceFactory.getUserService().checkUser(
				usernamePassword[0], usernamePassword[1]);
		
		if (user == null) {
			throw new NotAuthorizedException(
					"Provided user credentials are not valid.", BASIC);
		} else {
			requestContext.setProperty(BaseController.USER, user);
		}
		
	}
	
	/**
	 * Metoda vraća dekodirani Auth Basic header ili null ukoliko header nije
	 * ispravan.
	 * 
	 * @param authorization Base64 enkodirani username:passwrod
	 * @return Niz koji sadrži dekodirani username i password.
	 */
	private static String[] decodeAuthorization(String authorization) {
		if (authorization == null || !authorization.startsWith(BASIC)) {
			return null;
		}
		
		// Authorization: Basic base64credentials
		String base64Credentials = authorization.substring(BASIC.length())
				.trim();
		if (base64Credentials.isEmpty()) {
			return null;
		}
		
		// credentials = username:password
		String credentials = new String(java.util.Base64.getDecoder().decode(
				base64Credentials));
		
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
}
