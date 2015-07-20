package me.stronglift.api.service.inmemory;

import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import me.stronglift.api.error.ResourceNotFoundException;
import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.User;
import me.stronglift.api.service.BaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseServiceDummyImpl
 * 
 * @author Dusan Eremic
 *
 * @param <T>
 */
abstract class BaseServiceInMemoryImpl<T extends BaseEntity<T>> implements BaseService<T> {
	
	private static final Logger log = LoggerFactory.getLogger(BaseServiceInMemoryImpl.class);
	protected final List<T> data = new CopyOnWriteArrayList<>();
	protected Class<T> entityClasss;
	
	@SuppressWarnings("unchecked")
	public BaseServiceInMemoryImpl() {
		this.entityClasss = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		log.debug("{}InMemoryService initialized", getEntityName());
	}
	
	@Override
	public List<T> findAll(User user) {
		
		log.debug("Retrieving all {} entities for user {}", getEntityName(), user);
		
		List<T> listForUser = new LinkedList<>();
		
		for (T t : data) {
			if (t.getOwner().getId().equals(user.getId())) {
				listForUser.add(t);
			}
		}
		
		return listForUser;
	}
	
	@Override
	public T findOne(User user, String entityId) {
		log.debug("Retrieving one {} entity with ID {} for user {}", getEntityName(), entityId, user);
		
		for (T t : data) {
			if (t.getId().equals(entityId) && t.getOwner().getId().equals(user.getId())) {
				log.debug("An entity {} with ID {} is found, returning the result", getEntityName(), entityId);
				return t;
			}
		}
		
		log.debug("An entity {} with ID {} is not found, returning null", getEntityName(), entityId);
		
		return null;
	}
	
	@Override
	public T create(User user, T entity) {
		
		entity.setId(generateId());
		entity.getOwner().attach(user);
		
		log.debug("Creating a new entity {} with generated ID {}, owner {}", getEntityName(), entity.getId(), entity.getOwner());
		
		data.add(0, entity);
		
		log.debug("{} entity count: {}", getEntityName(), data.size());
		
		return entity;
	}
	
	@Override
	public T update(User user, T entity) {
		
		entity.getOwner().attach(user);
		
		log.debug("Updating an entity {} with ID {} for user {}", getEntityName(), entity.getId(), entity.getOwner());
		
		boolean elementFound = false;
		
		for (T t : data) {
			if (t.getId().equals(entity.getId()) && t.getOwner().getId().equals(user.getId())) {
				data.set(data.indexOf(t), entity);
				elementFound = true;
				break;
			}
		}
		
		if (!elementFound) {
			throw new ResourceNotFoundException(entityClasss, entity.getId());
		}
		
		log.debug("The {} entity with ID {} is updated successfully", getEntityName(), entity.getId());
		
		return entity;
	}
	
	@Override
	public boolean delete(User user, String entityId) {
		
		log.debug("Deliting {} entity with ID {} for user {}", getEntityName(), entityId, user);
		
		for (T t : data) {
			if (t.getId().equals(entityId) && t.getOwner().getId().equals(user.getId())) {
				log.debug("An entity {} with ID {} is found and deleted", getEntityName(), entityId);
				data.remove(t);
				log.debug("{} entity count: {}", getEntityName(), data.size());
				return true;
			}
		}
		
		log.debug("An entity {} with ID {} is not found!", getEntityName(), entityId);
		
		return false;
	}
	
	protected String getEntityName() {
		return this.entityClasss.getSimpleName();
	}
	
	protected static String generateId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
