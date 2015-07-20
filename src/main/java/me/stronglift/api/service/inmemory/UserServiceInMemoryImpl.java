package me.stronglift.api.service.inmemory;

import me.stronglift.api.error.UserAlreadyExistsException;
import me.stronglift.api.model.User;
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
		
		log.debug("Checking username and passwrod for user '{}'", username);
		
		for (User user : this.data) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				log.debug("Username and password are valid for user '{}'", username);
				return user;
			}
		}
		
		log.debug("Username and password are not valid for user '{}'", username);
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
		
		username = username.trim();
		password = password.trim();
		
		for (User user : this.data) {
			if (user.getUsername().equals(username)) {
				throw new UserAlreadyExistsException(username);
			}
		}
		
		User user = new User(username, password);
		user.setId(generateId());
		user.getOwner().attach(user);
		
		log.debug("Registering a new user '{}' with the generated ID {}", username, user.getId());
		data.add(user);
		log.debug("{} entity count: {}", getEntityName(), data.size());
		
		return user;
	}
}
