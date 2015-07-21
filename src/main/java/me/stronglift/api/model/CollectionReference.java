package me.stronglift.api.model;

/**
 * Rferenca na kolekciju entiteta.
 * 
 * @author Dusan Eremic
 */
public class CollectionReference<E extends BaseEntity<E>, P extends BaseEntity<P>> {
	
	/** Tip kolekcije */
	private final Class<E> collectionClass;
	
	/** Instanca parent entiteta */
	private final P parent;
	
	/**
	 * Constructor
	 * 
	 * @param collectionClass Tip kolekcije
	 * @param parent Instanca parent entiteta.
	 */
	public CollectionReference(Class<E> collectionClass, P parent) {
		
		if (collectionClass == null) {
			throw new RuntimeException(
					"The collection class parameter is null!");
		}
		
		if (parent == null) {
			throw new RuntimeException("The parent parameter is null!");
		}
		
		this.collectionClass = collectionClass;
		this.parent = parent;
	}
	
	/**
	 * @return Tip kolekcije
	 */
	public Class<E> getCollectionClass() {
		return collectionClass;
	}
	
	/**
	 * @return Parent tip
	 */
	@SuppressWarnings("unchecked")
	public Class<P> getParentClass() {
		return (Class<P>) this.parent.getClass();
	}
	
	/**
	 * @return Parent ID.
	 */
	public String getParentId() {
		return this.parent.getId();
	}
	
	/**
	 * @return Da li je referenca validna?
	 */
	public boolean isValid() {
		return getParentId() != null;
	}
}
