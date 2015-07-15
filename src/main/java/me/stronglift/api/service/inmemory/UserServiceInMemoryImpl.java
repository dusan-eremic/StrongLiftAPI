package me.stronglift.api.service.inmemory;

import me.stronglift.api.entity.User;
import me.stronglift.api.error.UserAlreadyExistsException;
import me.stronglift.api.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User service In-memory implementation
 * 
 * @author Dusan Eremic
 *
 */
class UserServiceInMemoryImpl extends BaseServiceInMemoryImpl<User> implements UserService {
	
	private static final Logger log = LoggerFactory.getLogger(UserServiceInMemoryImpl.class);
	
	@Override
	public User checkUser(String username, String password) {
		
		for (User user : this.data) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		
		return null;
	}
	
	@Override
	public User register(String username, String password) throws UserAlreadyExistsException {
		
		if (username == null || username.trim().isEmpty()) {
			throw new IllegalArgumentException("Username is not provided!");
		}
		
		if (password == null || password.trim().isEmpty()) {
			throw new IllegalArgumentException("Password is not provided!");
		}
		
		for (User user : this.data) {
			if (user.getUsername().equals(username)) {
				throw new UserAlreadyExistsException(username);
			}
		}
		
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setId(generateId());

		log.debug("Registering a new user '{}' with the generated ID {}", username, user.getId());
		data.add(user);
		log.debug("{} entity count: {}", getEntityName(), data.size());
		
		return user;
	}
}
