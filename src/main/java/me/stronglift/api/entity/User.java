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
	
	public boolean isUsernameValid() {
		
		if (username == null || username.trim().length() < 4) {
			return false;
		}
		
		return true;
	}
	
	public boolean isPasswordValid() {
		
		if (password == null || password.trim().length() < 4) {
			return false;
		}
		
		return true;
	}
}
