package me.stronglift.api.service;

import java.util.List;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.User;

/**
 * Osnovni servis sa zajedničkim operacijama.
 * 
 * @author Dusan Eremic
 *
 * @param <T> Tip eniteta sa kojim radi servis.
 */
public interface BaseService<T extends BaseEntity<T>> {
	
	/**
	 * Vraća sve entitete za korisnika.
	 */
	List<T> findAll(User user);
	
	/**
	 * Vraća entitet po ID-ju za korisnika.
	 */
	T findOne(User user, String entityId);
	
	/**
	 * Kreira novi entitet za korisnika.
	 */
	T create(User user, T entity);
	
	/**
	 * Ažurira entitet za korisnika.
	 */
	T update(User user, T entity);
	
	/**
	 * Briše entitet za korisnika.
	 */
	boolean delete(User user, String entityId);
}
