package me.stronglift.api.controller;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import me.stronglift.api.controller.resource.Resource;
import me.stronglift.api.controller.resource.ResourceMapper;
import me.stronglift.api.entity.User;
import me.stronglift.api.error.UserAlreadyExistsException;

/**
 * User resource
 * 
 * @author Dusan Eremic
 *
 */
@Path(ResourceMapper.Path.USER)
public class UserController extends BaseController {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(Resource<User> entityRequest) throws UserAlreadyExistsException, URISyntaxException {
		User user = entityRequest.toEntity(uriInfo, User.class);
		user = serviceFactory.getUserService().register(user.getUsername(), user.getPassword());
		return Response.created(new URI("https://www.facebook.com/")).build();
	}
}
