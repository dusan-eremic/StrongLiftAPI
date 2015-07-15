package me.stronglift.api.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * The base entity that will be extended by all persistent entities in the
 * application.
 * 
 * @author Dusan Eremic
 *
 * @param <T>
 *            Type of subclassed entity.
 */
@SuppressWarnings("serial")
public abstract class BaseEntity<T> implements Serializable {

	@Deserialize
	@Serialize
	protected String id;

	protected User owner;

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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * Creates a copy of this entity. TODO make a deep copy of collections
	 * 
	 * If entity contains {@link EntityReference}s, attached entity(s) will be
	 * discarded.
	 * 
	 * @param original
	 *            An original instance to be copied.
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BaseEntity [id=" + id + ", owner=" + owner + "]";
	}
}
