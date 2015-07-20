package me.stronglift.api.error;

import javax.ws.rs.NotFoundException;

import me.stronglift.api.model.BaseEntity;

/**
 * This exception shoud be thrown when a resource with specified ID is not found.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public class ResourceNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = 1L;
	
	public <T extends BaseEntity<T>> ResourceNotFoundException(Class<T> resourceClass, String invalidId) {
		super(String.format("%s for id '%s' cannot be found.", resourceClass.getSimpleName(), invalidId));
	}
	
}
