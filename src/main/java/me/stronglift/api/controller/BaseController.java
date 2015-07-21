package me.stronglift.api.controller;

import java.net.URI;

import javax.annotation.PostConstruct;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.User;
import me.stronglift.api.service.ServiceFactory;

/**
 * Base Jersey kontroler sa zajedničkim metodama.
 * 
 * @author Dušan Eremić
 *
 */
public abstract class BaseController {
	
	/**
	 * Key za User objekat smešten u ContainerRequestContext
	 */
	public static final String USER = "User";
	
	/**
	 * Injected service factory
	 */
	@Context
	protected ServiceFactory serviceFactory;
	
	/**
	 * Injected UriInfo
	 */
	@Context
	protected UriInfo uriInfo;
	
	/**
	 * User koji je napravio request
	 */
	protected User user;
	
	/**
	 * Injected request context
	 */
	@Context
	private ContainerRequestContext request;
	
	/**
	 * Metoda koja će biti pozvana nakon konstruktora i koja će uzeti User-a iz
	 * request contexta i smestiti ga u class field.
	 */
	@PostConstruct
	private void setUser() {
		if (request.getProperty(USER) != null) {
			user = (User) request.getProperty(USER);
		}
	}
	
	/**
	 * Metoda kreira "created" response
	 * 
	 * @param entity Entitet koji je kreiran.
	 * @return HTTP response 201 koji sadrži kreirani entitet i resource path u
	 *         HTTP header-u.
	 */
	protected <T extends BaseEntity<T>> Response created(T entity) {
		URI path = uriInfo.getAbsolutePathBuilder().path(entity.getId())
				.build();
		return Response.created(path)
				.entity(new Resource<>(uriInfo, LinkType.SELF, entity)).build();
	}
	
	/**
	 * Metoda kreira "updated" response
	 * 
	 * @param entity Entitet koji je ažuriran.
	 * @return HTTP response 200 koji sadrži ažurirani entiti i resource path u
	 *         HTTP header-u.
	 */
	protected <T extends BaseEntity<T>> Response updated(T entity) {
		return Response.ok()
				.entity(new Resource<>(uriInfo, LinkType.SELF, entity)).build();
	}
}
