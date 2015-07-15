package me.stronglift.api.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.stronglift.api.controller.resource.CollectionResource;
import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.controller.resource.ResourceMapper;
import me.stronglift.api.entity.Lift;
import me.stronglift.api.error.ResourceNotFoundException;

/**
 * Lift resource
 * 
 * @author Dusan Eremic
 *
 */
@Path(ResourceMapper.Path.LIFT)
public class LiftController extends BaseController {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Resource<Lift> entityRequest) {
		Lift entity = serviceFactory.getLiftService().create(user, entityRequest.toEntity(uriInfo, Lift.class));
		return created(entity);
	}
	
	@Path("/{id}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") String id, Resource<Lift> entityRequest) {
		Lift entity = entityRequest.toEntity(uriInfo, Lift.class);
		entity.setId(id);
		entity = serviceFactory.getLiftService().update(user, entity);
		return updated(entity);
	}
	
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CollectionResource<Lift> getAll() {
		return new CollectionResource<>(uriInfo, serviceFactory.getLiftService().findAll(user));
	}
	
	@Path("/{id}")
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Resource<Lift> getOne(@PathParam("id") String id) {
		
		Lift entity = serviceFactory.getLiftService().findOne(user, id);
		
		if (entity == null) {
			throw new ResourceNotFoundException(Lift.class, id);
		}
		
		return new Resource<>(uriInfo, LinkType.INSTANCE, entity);
	}
}
