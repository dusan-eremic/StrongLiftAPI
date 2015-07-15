package me.stronglift.api.controller.resource;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import me.stronglift.api.entity.BaseEntity;
import me.stronglift.api.entity.CollectionReference;
import me.stronglift.api.entity.EntityReference;
import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;

class EntityFieldMapper {
	
	private final HashMap<Class<?>, LinkedList<FieldHandler>> fieldHandlerMap;
	private final static EntityFieldMapper INSTANCE = new EntityFieldMapper();
	
	private EntityFieldMapper() {
		
		fieldHandlerMap = new HashMap<>();
		
		for (Class<?> entityClass : ResourceMapper.getResourceClasses()) {
			
			final LinkedList<FieldHandler> fieldListForClass = new LinkedList<>();
			fieldHandlerMap.put(entityClass, fieldListForClass);
			
			if (entityClass.getSuperclass() != null) {
				for (Field field : entityClass.getSuperclass().getDeclaredFields()) {
					fieldListForClass.add(new FieldHandler(field));
				}
			}
			
			for (Field field : entityClass.getDeclaredFields()) {
				fieldListForClass.add(new FieldHandler(field));
			}
			
		}
	}
	
	public <T extends BaseEntity<T>> List<FieldHandler> getAllFields(Class<T> entityClass) {
		return fieldHandlerMap.get(entityClass);
	}
	
	public <T extends BaseEntity<T>> List<FieldHandler> getSerializableFields(Class<T> entityClass) {
		return fieldHandlerMap.get(entityClass).stream().filter(handler -> handler.getField().isAnnotationPresent(Serialize.class))
				.collect(Collectors.toList());
	}
	
	public <T extends BaseEntity<T>> List<FieldHandler> getDeserializableFields(Class<T> entityClass) {
		return fieldHandlerMap.get(entityClass).stream().filter(handler -> handler.getField().isAnnotationPresent(Deserialize.class))
				.collect(Collectors.toList());
	}
	
	public static EntityFieldMapper get() {
		return INSTANCE;
	}
	
	public static class FieldHandler {
		
		private final Field field;
		private final MethodHandle getter;
		private final MethodHandle setter;
		
		public FieldHandler(Field field) {
			
			this.field = field;
			field.setAccessible(true);
			
			try {
				getter = MethodHandles.lookup().unreflectGetter(field);
				setter = MethodHandles.lookup().unreflectSetter(field);
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		public Field getField() {
			return field;
		}
		
		public boolean isEntityReference() {
			return EntityReference.class.isAssignableFrom(field.getType());
		}
		
		public boolean isCollectionReference() {
			return CollectionReference.class.isAssignableFrom(field.getType());
		}
		
		public boolean isString() {
			return String.class.isAssignableFrom(field.getType());
		}
		
		public boolean isInstant() {
			return Instant.class.isAssignableFrom(field.getType());
		}
		
		public boolean isInteger() {
			return Integer.class.isAssignableFrom(field.getType());
		}
		
		public boolean isDouble() {
			return Double.class.isAssignableFrom(field.getType());
		}
		
		public boolean isBigDecimal() {
			return BigDecimal.class.isAssignableFrom(field.getType());
		}
		
		public boolean isEnum() {
			return field.getType().isEnum();
		}
		
		public <T extends BaseEntity<T>> Object getValue(T entity) {
			try {
				return this.getter.invoke(entity);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		
		public <T extends BaseEntity<T>> void setValue(T entity, Object value) {
			try {
				this.setter.invoke(entity, value);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}
}
