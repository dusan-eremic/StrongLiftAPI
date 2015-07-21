package me.stronglift.api.model;

import java.io.Serializable;

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

/**
 * Osnovni entitet u aplikaciji.
 * 
 * @author Dusan Eremic
 *
 * @param <T> Tip podklase.
 */
@SuppressWarnings("serial")
public abstract class BaseEntity<T> implements Serializable {
	
	/**
	 * ID
	 */
	@Deserialize
	@Serialize
	protected String id;
	
	/**
	 * Referenca na vlasnika
	 */
	@Serialize
	protected EntityReference<User> owner = new EntityReference<User>(
			User.class);
	
	/**
	 * @return ID
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * @param id ID
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return Referenca na vlasnika
	 */
	public EntityReference<User> getOwner() {
		return owner;
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
