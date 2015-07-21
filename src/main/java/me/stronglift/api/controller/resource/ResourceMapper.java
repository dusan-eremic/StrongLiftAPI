package me.stronglift.api.controller.resource;

import java.util.ArrayList;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.Lift;
import me.stronglift.api.model.User;

/**
 * Mapiranje model klasa sa putanjama koje će se koristiti za pristup resurima
 * tog tipa.
 * 
 * @author Dusan Eremic
 *
 */
public enum ResourceMapper {
	
	/** Lift resurs */
	lifts(Path.LIFT, Lift.class),
	/** User resurs */
	users(Path.USER, User.class);
	
	/**
	 * Predefinisane putanje do resursa
	 */
	public static class Path {
		public static final String LIFT = "/lifts";
		public static final String USER = "/users";
	}
	
	/** Resource path */
	final private String resourcePath;
	
	/** Tip resursa */
	final private Class<?> entityClass;
	
	/**
	 * Private constructor
	 * 
	 * @param resourcePath {@link Path}
	 * @param entityClass Tip resursa
	 */
	private <T extends BaseEntity<T>> ResourceMapper(String resourcePath,
			Class<T> entityClass) {
		this.resourcePath = resourcePath;
		this.entityClass = entityClass;
	}
	
	/**
	 * Vraća path za prosleđeni resurs tip.
	 * 
	 * @param entityClass Tip resursa.
	 * @return Resource path.
	 */
	public static <T extends BaseEntity<T>> String getPathForResourceClass(
			Class<T> entityClass) {
		for (ResourceMapper resourcePath : values()) {
			if (resourcePath.entityClass.isAssignableFrom(entityClass)) {
				return resourcePath.getResourcePath();
			}
		}
		throw new IllegalArgumentException("No ResourcePath for class '"
				+ entityClass.getName() + "'");
	}
	
	/**
	 * @return Vraća listu tipova svih resursa.
	 */
	public static ArrayList<Class<?>> getResourceClasses() {
		
		ArrayList<Class<?>> classes = new ArrayList<>();
		
		for (ResourceMapper resource : ResourceMapper.values()) {
			classes.add(resource.getResourceClass());
		}
		
		return classes;
	}
	
	/**
	 * @return Vraća tip resursa.
	 */
	public Class<?> getResourceClass() {
		return entityClass;
	}
	
	/**
	 * @return Vraća path resursa.
	 */
	public String getResourcePath() {
		return resourcePath;
	}
}
