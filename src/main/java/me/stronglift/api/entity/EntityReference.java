package me.stronglift.api.entity;

import javax.lang.model.type.ReferenceType;

/**
 * This class represents a reference to another {@link BaseEntity} - it is holding its ID and optionally the full instance of the referenced entity.
 * 
 * When used inside a {@link BaseEntity}, it should be initialized as a final field and mutated trough setter method and cleared with clear() method.
 * 
 * @author Dusan Eremic
 */
public class EntityReference<T extends BaseEntity<T>> implements Comparable<EntityReference<T>> {
	
	/** ID of the referenced entity (mandatory) */
	private String id = null;
	
	/** Entity class for type T */
	private Class<T> entityClass;
	
	/** Full instance of the referenced entity */
	private T entityInstance = null;
	
	/**
	 * Default constructor
	 */
	public EntityReference() {
		
	}
	
	/**
	 * Constructor #1
	 * 
	 * @param entityClass Entity class for type T
	 */
	public EntityReference(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * Constructor #2
	 * 
	 * The reference type will be set to {@link Referenc} since we have ID of an instance.
	 * 
	 * @param entityClass Entity class for type T
	 * @param entityInstanceId Entity ID.
	 */
	public EntityReference(Class<T> entityClass, String entityInstanceId) {
		this.entityClass = entityClass;
		this.id = entityInstanceId;
	}
	
	/**
	 * Constructor #3
	 * 
	 * The reference type will be set to {@link ReferenceType#INSTANCE} since we have entity instance.
	 * 
	 * @param entityClass Entity class for type T
	 * @param entityInstance Entity instance
	 */
	public EntityReference(Class<T> entityClass, T entityInstance) {
		this.entityClass = entityClass;
		this.id = entityInstance.getId();
		this.entityInstance = entityInstance;
	}
	
	/**
	 * Constructor #4 (Copy constructor)
	 * 
	 * @param originalReference A reference to be copied.
	 */
	public EntityReference(final EntityReference<T> originalReference) {
		this.entityClass = originalReference.entityClass;
		this.id = originalReference.id;
		this.entityInstance = originalReference.entityInstance;
	}
	
	/**
	 * Returns entity class (for type T) which is supplied when this entity reference is created.
	 * 
	 * @return Entity class
	 */
	
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	/**
	 * Set ID of the referenced entity.
	 * 
	 * @param id Referenced entity ID.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Returns ID of the referenced entity.
	 * 
	 * @return Referenced entity ID.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Attaches an entity instance and its ID to this entity reference.
	 * 
	 * @param entityInstance The full instance of the referenced entity.
	 */
	public void attach(final T entityInstance) {
		this.entityInstance = entityInstance;
		
		if (entityInstance != null) {
			this.id = entityInstance.getId();
		} else {
			this.id = null;
		}
	}
	
	/**
	 * Detaches an entity instance from this reference.
	 * 
	 */
	public void detach() {
		this.entityInstance = null;
	}
	
	/**
	 * Returns the full instance of a referenced entity.
	 * 
	 * @return Entity instance.
	 */
	public T getEntity() {
		return this.entityInstance;
	}
	
	public void setEntityClass(Class<T> clazz) {
		this.entityClass = clazz;
	}
	
	/**
	 * Has the entity reference been set?
	 * 
	 * @return true if the reference has been set, false otherwise.
	 */
	public boolean hasFullEntity() {
		return this.entityInstance != null;
	}
	
	/**
	 * Is this entity reference valid i.e. has the entity ID been set?
	 * 
	 * @return true if entity reference is valid, otherwise false
	 */
	public boolean isValid() {
		return (this.id != null);
	}
	
	/**
	 * Clears ID and attached entity instance (if set).
	 */
	public void clear() {
		this.id = null;
		this.entityInstance = null;
	}
	
	/**
	 * Compares two EntityReferences by ID.
	 */
	@Override
	public int compareTo(EntityReference<T> o) {
		return this.id.compareTo(o.id);
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (!(obj instanceof EntityReference))
			return false;
		
		// Safe cast
		final EntityReference<?> otherReference = (EntityReference<?>) obj;
		
		// Check the class of a referenced entity
		if (!this.entityClass.equals(otherReference.entityClass)) {
			return false;
		}
		
		// If ID is null no need to compare
		if (id == null)
			return false;
		
		// Compare IDs
		return id.equals(otherReference.id);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityClass == null) ? 0 : entityClass.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	/**
	 * toString
	 */
	@Override
	public String toString() {
		return "EntityReference [id=" + id + ", entityClass=" + entityClass + ", entityInstance=" + entityInstance + "]";
	}
	
}
