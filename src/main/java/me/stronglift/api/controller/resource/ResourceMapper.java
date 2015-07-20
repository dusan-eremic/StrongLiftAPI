package me.stronglift.api.controller.resource;

import java.util.ArrayList;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.Lift;
import me.stronglift.api.model.User;

/**
 * All resource paths must be mapped here.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public enum ResourceMapper {
	
	lifts(Path.LIFT, Lift.class), //
	users(Path.USER, User.class);
	
	final private String resourcePath;
	final private Class<?> entityClass;
	
	private <T extends BaseEntity<T>> ResourceMapper(String resourcePath, Class<T> entityClass) {
		this.resourcePath = resourcePath;
		this.entityClass = entityClass;
	}
	
	public static <T extends BaseEntity<T>> String getPathForResourceClass(Class<T> entityClass) {
		for (ResourceMapper resourcePath : values()) {
			if (resourcePath.entityClass.isAssignableFrom(entityClass)) {
				return resourcePath.getResourcePath();
			}
		}
		throw new IllegalArgumentException("No ResourcePath for class '" + entityClass.getName() + "'");
	}
	
	public static ArrayList<Class<?>> getResourceClasses() {
		
		ArrayList<Class<?>> classes = new ArrayList<>();
		
		for (ResourceMapper resource : ResourceMapper.values()) {
			classes.add(resource.getResourceClass());
		}
		
		return classes;
	}
	
	public Class<?> getResourceClass() {
		return entityClass;
	}
	
	public String getResourcePath() {
		return resourcePath;
	}
	
	public static class Path {
		public static final String LIFT = "/lifts";
		public static final String USER = "/users";
	}
	
}
