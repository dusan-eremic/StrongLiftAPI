package me.stronglift.api.error;

import me.stronglift.api.model.BaseEntity;

/**
 * Ovaj exception će biti bačen prilikom registracije korisnika sa username-om
 * koji već postoji.
 * 
 * @author Dusan Eremic
 *
 */
public class UserAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = -5623139569216073398L;
	
	/**
	 * Constructor
	 * 
	 * @param username Korisničko ime koje je već zauzeto.
	 */
	public <T extends BaseEntity<T>> UserAlreadyExistsException(String username) {
		super(String.format("The username '%s' is already taken.", username));
	}
	
}
