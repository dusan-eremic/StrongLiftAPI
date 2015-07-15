package me.stronglift.api.error;

import me.stronglift.api.entity.BaseEntity;

public class UserAlreadyExistsException extends Exception {
	
	private static final long serialVersionUID = -5623139569216073398L;
	
	public <T extends BaseEntity<T>> UserAlreadyExistsException(String username) {
		super(String.format("The username '%s' is already taken.", username));
	}
	
}
