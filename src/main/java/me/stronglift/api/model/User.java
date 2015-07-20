package me.stronglift.api.model;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * User entity
 * 
 * @author Dusan Eremic
 *
 */
public class User extends BaseEntity<User> {
	
	private static final long serialVersionUID = -4807104914667556929L;
	
	@Deserialize
	@Serialize
	private String username;
	
	@Deserialize
	private String password;
	
	@Serialize
	private CollectionReference<Lift, User> lifts = new CollectionReference<>(Lift.class, this);
	
	public User() {
		owner.setSerializable(false);
	}
	
	public User(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [username=" + username + ", id=" + id + "]";
	}
}
