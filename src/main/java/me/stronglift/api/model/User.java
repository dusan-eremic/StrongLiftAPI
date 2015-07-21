package me.stronglift.api.model;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * User model
 * 
 * @author Dusan Eremic
 *
 */
public class User extends BaseEntity<User> {
	
	private static final long serialVersionUID = -4807104914667556929L;
	
	/**
	 * Korisničko ime
	 */
	@Deserialize
	@Serialize
	private String username;
	
	/**
	 * Lozinka
	 */
	@Deserialize
	private String password;
	
	/**
	 * Referenca na listu {@link Lift}ova koje je uneo korisnik.
	 */
	@Serialize
	private CollectionReference<Lift, User> lifts = new CollectionReference<>(
			Lift.class, this);
	
	/**
	 * Konstruktor 1
	 */
	public User() {
		owner.setSerializable(false);
	}
	
	/**
	 * Konstruktor 2
	 */
	public User(String username, String password) {
		this();
		this.username = username;
		this.password = password;
	}
	
	// GET i SET metode START
	
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
	
	// GET i SET metode END
	
	@Override
	public String toString() {
		return "User [username=" + username + ", id=" + id + "]";
	}
}
