package me.stronglift.api.model;

/**
 * Predstavlja referencu na drugi {@link BaseEntity} - sadrži njen ID i opciono
 * objektnu referencu na konkretnu instancu.
 * 
 * @author Dusan Eremic
 */
public class EntityReference<T extends BaseEntity<T>> implements
		Comparable<EntityReference<T>> {
	
	/** ID referenciranog entiteta */
	private String id = null;
	
	/** Tip referenciranog entiteta */
	private Class<T> entityClass;
	
	/** Referenca na ceo entitet */
	private T entityInstance = null;
	
	/** Da li će referenca biti serijalizovana? */
	private boolean serializable = true;
	
	/**
	 * Constructor
	 * 
	 * @param entityClass Tip referenciranog entiteta
	 */
	public EntityReference(Class<T> entityClass) {
		this.entityClass = entityClass;
	}
	
	/**
	 * @return Tip referenciranog entiteta
	 */
	public Class<T> getEntityClass() {
		return entityClass;
	}
	
	/**
	 * @param id Tip referenciranog entiteta
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return ID referenciranog entiteta
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @return Da li će referenca biti serijalizovana?
	 */
	public boolean isSerializable() {
		return serializable;
	}
	
	/**
	 * @param serializable Da li će referenca biti serijalizovana?
	 */
	public void setSerializable(boolean serializable) {
		this.serializable = serializable;
	}
	
	/**
	 * Dodaje konkretan entitet u ovu referencu.
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
	 * Uklanja konkretan entitet iz ove reference.
	 */
	public void detach() {
		this.entityInstance = null;
	}
	
	/**
	 * @return Vraća pridruženi entitet.
	 */
	public T getEntity() {
		return this.entityInstance;
	}
	
	/**
	 * @return Da li je entitet pridružen ovoj referenci?
	 */
	public boolean hasFullEntity() {
		return this.entityInstance != null;
	}
	
	/**
	 * @return Da li je referenca validna?
	 */
	public boolean isValid() {
		return (this.id != null);
	}
	
	/**
	 * Briše referencu i dovodi je u nevalidno stanje.
	 */
	public void clear() {
		this.id = null;
		this.entityInstance = null;
	}
	
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
		result = prime * result
				+ ((entityClass == null) ? 0 : entityClass.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
		return "EntityReference [id=" + id + ", entityClass=" + entityClass
				+ ", entityInstance=" + entityInstance + "]";
	}
	
}
