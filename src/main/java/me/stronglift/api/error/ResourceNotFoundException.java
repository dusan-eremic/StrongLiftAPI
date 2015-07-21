package me.stronglift.api.error;

import javax.ws.rs.NotFoundException;

import me.stronglift.api.model.BaseEntity;

/**
 * Ovaj exception će biti bačen kada traženi resurs ne postoji.
 * 
 * @author Dusan Eremic
 *
 */
public class ResourceNotFoundException extends NotFoundException {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * 
	 * @param resourceClass Tip resursa.
	 * @param invalidId ID ressura koji nije pronađen.
	 **/
	public <T extends BaseEntity<T>> ResourceNotFoundException(
			Class<T> resourceClass, String invalidId) {
		super(String.format("%s for id '%s' cannot be found.",
				resourceClass.getSimpleName(), invalidId));
	}
	
}
