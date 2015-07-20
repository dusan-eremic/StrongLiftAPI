package me.stronglift.api.model;

/**
 * This class represents a reference to another {@link BaseEntity} - it is holding its ID and optionally the full instance of the referenced collection.
 * 
 * When used inside a {@link BaseEntity}, it shuold be initialzied as a final field and mutated trough setter method and cleared with clear() method.
 * 
 * @author Dusan Eremic
 */
public class CollectionReference<E extends BaseEntity<E>, P extends BaseEntity<P>> {
	
	/** Collection class for type T */
	private final Class<E> collectionClass;
	
	/** Reference to the parent entity */
	private final P parent;
	
	/**
	 * Constructor #1
	 * 
	 * @param collectionClass Collection class for type T
	 */
	public CollectionReference(Class<E> collectionClass, P parent) {
		
		if (collectionClass == null) {
			throw new RuntimeException("The collection class parameter is null!");
		}
		
		if (parent == null) {
			throw new RuntimeException("The parent parameter is null!");
		}
		
		this.collectionClass = collectionClass;
		this.parent = parent;
	}
	
	/**
	 * Constructor #2 (Copy constructor)
	 * 
	 * @param originalReference A reference to be copied.
	 */
	public CollectionReference(final CollectionReference<E, P> originalReference) {
		this.collectionClass = originalReference.collectionClass;
		this.parent = originalReference.parent;
	}
	
	/**
	 * Returns collection class (for type T) which is supplied when this collection reference is created.
	 * 
	 * @return Collection class
	 */
	public Class<E> getCollectionClass() {
		return collectionClass;
	}
	
	/**
	 * Returns a class of the parent entity.
	 * 
	 * @return A class of the parent entity.
	 */
	@SuppressWarnings("unchecked")
	public Class<P> getParentClass() {
		return (Class<P>) this.parent.getClass();
	}
	
	/**
	 * Returns ID of the parent reference of this collection reference.
	 * 
	 * @return Parent ID.
	 */
	public String getParentId() {
		return this.parent.getId();
	}
	
	/**
	 * Is this collection reference valid i.e. has the collection ID been set?
	 * 
	 * @return true if collection reference is valid, otherwise false
	 */
	public boolean isValid() {
		return getParentId() != null;
	}
}
