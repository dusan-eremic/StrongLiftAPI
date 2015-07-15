package me.stronglift.api.entity;

import me.stronglift.api.entity.annotation.Deserialize;

/**
 * User entity
 * 
 * @author Dusan Eremic
 *
 */
public class User extends BaseEntity<User> {

	private static final long serialVersionUID = -4807104914667556929L;

	@Deserialize
	private String username;

	@Deserialize
	private String password;

	public User() {

	}

	public User(String username, String password) {
		super();
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
