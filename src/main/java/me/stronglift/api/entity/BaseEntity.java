package me.stronglift.api.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * The base entity that will be extended by all persistent entities in the application.
 * 
 * @author Dusan Eremic
 *
 * @param <T> Type of subclassed entity.
 */
@SuppressWarnings("serial")
public abstract class BaseEntity<T> implements Serializable {
	
	@Deserialize
	@Serialize
	private String id;
	
	/**
	 * ID of an entity that matches the DB primary key.
	 * 
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * ID of an entity that matches the DB primary key.
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Creates a copy of this entity. TODO make a deep copy of collections
	 * 
	 * If entity contains {@link EntityReference}s, attached entity(s) will be discarded.
	 * 
	 * @param original An original instance to be copied.
	 * @return A copy of the original instance.
	 */
	@SuppressWarnings("unchecked")
	public T copy() {
		
		T copy = null;
		
		try {
			copy = (T) this.getClass().newInstance();
			
			for (Field field : this.getClass().getDeclaredFields()) {
				field.setAccessible(true);
				field.set(copy, field.get(this));
			}
			
		} catch (InstantiationException | IllegalAccessException e) {
			
		}
		
		return copy;
	}
	
}
