package me.stronglift.api.service;

import me.stronglift.api.error.UserAlreadyExistsException;
import me.stronglift.api.model.User;

/**
 * Operacije specifične za {@link User} entitet.
 * 
 * @author Dusan Eremic
 *
 */
public interface UserService extends BaseService<User> {
	
	/**
	 * Autentifikuje korisnika, vraća User instancu ukoliko je autentifikacija
	 * uspela, ako nije varća null.
	 */
	User checkUser(String username, String password);
	
	/**
	 * Registruje novog korisnika.
	 * 
	 * @return User instancu ukoliko je registracija uspela.
	 * @throws UserAlreadyExistsException Ukoliko je username zauzet.
	 */
	User register(String username, String password)
			throws UserAlreadyExistsException;
	
}
