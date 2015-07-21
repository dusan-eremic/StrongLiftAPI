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

import me.stronglift.api.entity.annotation.Deserialize;
import me.stronglift.api.entity.annotation.Serialize;
import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.CollectionReference;
import me.stronglift.api.model.EntityReference;

/**
 * EntityFieldMapper koristi Java refleksiju da skenira sva polja svih model
 * klasa mapiranih u {@link ResourceMapper}-u i potom može za svaku model klasu
 * da vrati sva polja, sva @Serialize polja ili sva @Deserialize polja.
 * 
 * @author Dusan Eremic
 */
class EntityFieldMapper {
	
	/** Mapa koja čuva sva polja svih model klasa */
	private final HashMap<Class<?>, LinkedList<FieldHandler>> fieldHandlerMap;
	
	/** Singleton instanca EntityFieldMapper-a */
	private final static EntityFieldMapper INSTANCE = new EntityFieldMapper();
	
	/**
	 * Private constructor
	 */
	private EntityFieldMapper() {
		
		fieldHandlerMap = new HashMap<>();
		
		for (Class<?> entityClass : ResourceMapper.getResourceClasses()) {
			
			final LinkedList<FieldHandler> fieldListForClass = new LinkedList<>();
			fieldHandlerMap.put(entityClass, fieldListForClass);
			
			if (entityClass.getSuperclass() != null) {
				for (Field field : entityClass.getSuperclass()
						.getDeclaredFields()) {
					fieldListForClass.add(new FieldHandler(field));
				}
			}
			
			for (Field field : entityClass.getDeclaredFields()) {
				fieldListForClass.add(new FieldHandler(field));
			}
			
		}
	}
	
	/**
	 * Vraća sva polja za prosleđenu klasu.
	 * 
	 * @param entityClass Klasa tipa T extends BaseEntity<T>.
	 * @return Sva polja za prosleđenu klasu.
	 */
	public <T extends BaseEntity<T>> List<FieldHandler> getAllFields(
			Class<T> entityClass) {
		return fieldHandlerMap.get(entityClass);
	}
	
	/**
	 * Vraća sva @Serialize polja za prosleđenu klasu.
	 * 
	 * @param entityClass Klasa tipa T extends BaseEntity<T>.
	 * @return Polja prosleđene klase anotirana sa @Serialize.
	 */
	public <T extends BaseEntity<T>> List<FieldHandler> getSerializableFields(
			Class<T> entityClass) {
		return fieldHandlerMap
				.get(entityClass)
				.stream()
				.filter(handler -> handler.getField().isAnnotationPresent(
						Serialize.class)).collect(Collectors.toList());
	}
	
	/**
	 * Vraća sva @Deserialize polja za prosleđenu klasu.
	 * 
	 * @param entityClass Klasa tipa T extends BaseEntity<T>.
	 * @return Polja prosleđene klase anotirana sa @Deserialize.
	 */
	public <T extends BaseEntity<T>> List<FieldHandler> getDeserializableFields(
			Class<T> entityClass) {
		return fieldHandlerMap
				.get(entityClass)
				.stream()
				.filter(handler -> handler.getField().isAnnotationPresent(
						Deserialize.class)).collect(Collectors.toList());
	}
	
	/**
	 * Vraća singleton instancu EntityFieldMapper-a.
	 */
	public static EntityFieldMapper get() {
		return INSTANCE;
	}
	
	/**
	 * FieldHandler predstavlja wrapper za {@link Field} sa dodatim getValue i
	 * setValue metodama, kao i uslužnim metodama za proveru tipa.
	 * 
	 * @author Dusan Eremic
	 */
	public static class FieldHandler {
		
		/** Instanca originalnog polja */
		private final Field field;
		
		/** Getter handler */
		private final MethodHandle getter;
		
		/** Setter handler */
		private final MethodHandle setter;
		
		/**
		 * Constructor
		 * 
		 * @param field Instanca originalnog polja.
		 */
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
		
		/**
		 * Vraca instancu originalnog polja.
		 */
		public Field getField() {
			return field;
		}
		
		// ###### Set metoda za proveru tipa wrapp-ovanog polja START
		
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
		
		// ###### Set metoda za proveru tipa wrapp-ovanog polja END
		
		/**
		 * Getter handler za polje.
		 * 
		 * @param entity Instanca kojoj se čita vrednost iz polja.
		 * @return Object Pročitana vrednost.
		 */
		public <T extends BaseEntity<T>> Object getValue(T entity) {
			try {
				return this.getter.invoke(entity);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
		
		/**
		 * Setter handler za polje
		 * 
		 * @param entity Instanca kojoj se setuje vrednost u polje.
		 * @param value Vrednost koja se setuje.
		 */
		public <T extends BaseEntity<T>> void setValue(T entity, Object value) {
			try {
				this.setter.invoke(entity, value);
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		}
	}
}
