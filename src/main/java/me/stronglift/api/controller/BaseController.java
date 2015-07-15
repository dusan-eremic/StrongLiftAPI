package me.stronglift.api.controller;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.entity.BaseEntity;
import me.stronglift.api.entity.User;
import me.stronglift.api.service.ServiceFactory;

/**
 * The base controller implementing common methods and it will be extended by all other controllers.
 * 
 * @author Dušan Eremić
 *
 */
public abstract class BaseController {
	
	public static final String USER = "User";
	
	@Context
	protected ServiceFactory serviceFactory;
	
	@Context
	protected UriInfo uriInfo;
	
	protected User user;
	
	@Context
	private ContainerRequestContext request;
	
	@PostConstruct
	private void setUser() {
		if (request.getProperty(USER) != null) {
			user = (User) request.getProperty(USER);
		}
	}
	
	protected <T extends BaseEntity<T>> Response created(T entity) {
		return Response.created(URI.create(uriInfo.getAbsolutePath().toString())).entity(new Resource<>(uriInfo, LinkType.SELF, entity)).build();
	}
	
	protected <T extends BaseEntity<T>> Response updated(T entity) {
		return Response.ok().entity(new Resource<>(uriInfo, LinkType.SELF, entity)).build();
	}
}
