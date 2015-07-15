package me.stronglift.api.service.inmemory;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.ws.rs.NotFoundException;

import me.stronglift.api.entity.BaseEntity;
import me.stronglift.api.entity.User;
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
		log.debug("{}ServiceDummy initialized", getEntityName());
	}
	
	@Override
	public List<T> findAll(User user) {
		
		log.debug("Retrieving {} entities...", getEntityName());
		
		return data;
	}
	
	@Override
	public T findOne(User user, String entityId) {
		log.debug("Retrieving an entity {} with ID {}", getEntityName(), entityId);
		
		for (T t : data) {
			if (t.getId().equals(entityId)) {
				log.debug("The entity {} with ID {} is found, returning the result", getEntityName(), entityId);
				return t.copy();
			}
		}
		
		log.debug("The entity {} with ID {} is not found, returning null", getEntityName(), entityId);
		return null;
	}
	
	@Override
	public T create(User user, T entity) {
		
		entity.setId(generateId());
		
		log.debug("Creating a new entity {} with generated ID {}", getEntityName(), entity.getId());
		
		data.add(entity);
		
		log.debug("{} entity count: {}", getEntityName(), data.size());
		
		return entity;
	}
	
	@Override
	public T update(User user, T entity) {
		
		log.debug("Updating an entity {} with ID {}", getEntityName(), entity.getId());
		
		boolean elementFound = false;
		
		for (T t : data) {
			if (t.getId().equals(entity.getId())) {
				data.add(data.indexOf(t), entity);
				elementFound = true;
				break;
			}
		}
		
		if (!elementFound) {
			String message = String.format("An entity %s with ID %s cannot be found.", getEntityName(), entity.getId());
			log.debug(message);
			throw new NotFoundException(message);
		}
		
		log.debug("The {} entity with ID {} is updated successfully", getEntityName(), entity.getId());
		
		return entity;
	}
	
	protected String getEntityName() {
		return this.entityClasss.getSimpleName();
	}
	
	protected static String generateId() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
}
