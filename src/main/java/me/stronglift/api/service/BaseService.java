package me.stronglift.api.service;

import java.util.List;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.User;

/**
 * Base interface for common operations for all entities.
 * 
 * @author Dusan Eremic
 *
 * @param <T>
 *            Type of an entity handled by the service.
 */
public interface BaseService<T extends BaseEntity<T>> {

	List<T> findAll(User user);

	T findOne(User user, String entityId);

	T create(User user, T entity);

	T update(User user, T entity);

	boolean delete(User user, String entityId);
}
