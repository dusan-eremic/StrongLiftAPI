package me.stronglift.api.service;

import me.stronglift.api.error.UserAlreadyExistsException;
import me.stronglift.api.model.User;

/**
 * User service interface
 * 
 * @author Dusan Eremic
 *
 */
public interface UserService extends BaseService<User> {
	
	User checkUser(String username, String password);
	
	User register(String username, String password) throws UserAlreadyExistsException;
	
}
