package me.stronglift.api.service;

import java.util.List;

import me.stronglift.api.entity.BaseEntity;
import me.stronglift.api.entity.User;

/**
 * Base interface for common operations for all entities.
 * 
 * @author Dusan Eremic
 *
 * @param <T> Type of an entity handled by the service.
 */
public interface BaseService<T extends BaseEntity<T>> {
	
	public List<T> findAll(User user);
	
	public T findOne(User user, String entityId);
	
	public T create(User user, T entity);
	
	public T update(User user, T entity);
}
