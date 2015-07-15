package me.stronglift.api.service;

import me.stronglift.api.entity.User;
import me.stronglift.api.error.UserAlreadyExistsException;

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
