package me.stronglift.api.controller.resource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashMap;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.UriInfo;

import me.stronglift.api.controller.resource.EntityFieldMapper.FieldHandler;
import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.entity.BaseEntity;
import me.stronglift.api.entity.CollectionReference;
import me.stronglift.api.entity.EntityReference;
import me.stronglift.api.error.ConversionException;
import me.stronglift.api.util.Convert;
import me.stronglift.api.util.StringUtils;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * The base resource class.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public class Resource<T extends BaseEntity<T>> extends ResourceMeta {
	
	private static final long serialVersionUID = 8796166223133531134L;
	
	public Resource() {
		super();
	}
	
	public <R extends BaseEntity<R>> Resource(final UriInfo uriInfo, final T entity) {
		this(uriInfo, null, entity);
	}
	
	public <R extends BaseEntity<R>> Resource(final UriInfo uriInfo, final LinkType linkType, final T entity) {
		
		super(linkType != null ? ResourceLink.link(uriInfo.getAbsolutePath(), linkType, entity) : null);
		
		@SuppressWarnings("unchecked")
		final Class<T> entityClass = (Class<T>) (Class<?>) entity.getClass();
		
		for (FieldHandler fieldHandler : EntityFieldMapper.get().getSerializableFields(entityClass)) {
			
			final String fieldName = fieldHandler.getField().getName();
			final Object fieldValue = fieldHandler.getValue(entity);
			
			if (fieldHandler.isEntityReference()) {
				
				@SuppressWarnings("unchecked")
				final EntityReference<R> entityReference = (EntityReference<R>) fieldValue;
				
				if (entityReference != null && entityReference.hasFullEntity()) {
					put(fieldName, new Resource<>(uriInfo, LinkType.INSTANCE, entityReference.getEntity()));
				} else if (entityReference != null && entityReference.isValid()) {
					put(fieldName, new ResourceMeta(ResourceLink.link(uriInfo.getBaseUri(), LinkType.INSTANCE, entityReference)));
				} else {
					put(fieldName, null);
				}
			} else if (fieldHandler.isCollectionReference()) {
				
				@SuppressWarnings("unchecked")
				final CollectionReference<R, T> collectionReference = (CollectionReference<R, T>) fieldValue;
				put(fieldName, new ResourceMeta(ResourceLink.link(uriInfo.getBaseUri(), LinkType.COLLECTION, collectionReference)));
				
			} else if (fieldHandler.isString()) {
				put(fieldName, fieldValue);
			} else if (fieldHandler.isInstant()) {
				put(fieldName, Convert.toString((Instant) fieldValue));
			} else if (fieldHandler.isDouble()) {
				put(fieldName, Convert.toString((Double) fieldValue, 2));
			} else if (fieldHandler.isBigDecimal()) {
				put(fieldName, Convert.toString((BigDecimal) fieldValue, 2));
			} else if (fieldHandler.isInteger()) {
				put(fieldName, fieldValue);
			} else {
				throw new RuntimeException(String.format("Unsupported field type '%s' for field '%s.%s'", fieldHandler.getField().getType().getSimpleName(),
						entity.getClass().getSimpleName(), fieldName));
			}
		}
	}
	
	public T toEntity(UriInfo uriInfo, Class<T> entityClass) {
		
		T entity;
		
		try {
			entity = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new BadRequestException(e);
		}
		
		for (FieldHandler fieldHandler : EntityFieldMapper.get().getDeserializableFields(entityClass)) {
			
			final String fieldName = fieldHandler.getField().getName();
			final Object fieldValue = get(fieldName);
			
			try {
				if (fieldValue != null) {
					if (fieldHandler.isEntityReference()) {
						EntityReference<?> er = (EntityReference<?>) fieldHandler.getValue(entity);
						er.setId(getIdForResource(fieldName, uriInfo, er.getEntityClass()));
					} else if (fieldHandler.isString()) {
						fieldHandler.setValue(entity, Convert.toString(fieldValue));
					} else if (fieldHandler.isInstant()) {
						fieldHandler.setValue(entity, Convert.toInstant(fieldValue));
					} else if (fieldHandler.isInteger()) {
						fieldHandler.setValue(entity, Convert.toInt(fieldValue));
					} else if (fieldHandler.isDouble()) {
						fieldHandler.setValue(entity, Convert.toDouble(fieldValue));
					} else if (fieldHandler.isBigDecimal()) {
						fieldHandler.setValue(entity, Convert.toBigDecimal(fieldValue));
					} else {
						throw new RuntimeException(String.format("Unsupported field type '%s' for field '%s.%s'", fieldHandler.getField().getType()
								.getSimpleName(), entityClass.getSimpleName(), fieldName));
					}
				}
			} catch (ConversionException ce) {
				ce.setField(fieldName);
				throw new BadRequestException(ce);
			}
		}
		
		return entity;
	}
	
	private <C extends BaseEntity<C>> String getIdForResource(String fieldName, UriInfo uriInfo, Class<C> resourceClass) {
		
		@SuppressWarnings("unchecked")
		LinkedHashMap<String, String> resourceMeta = ((LinkedHashMap<String, String>) get(fieldName));
		final String resourceHref = resourceMeta.get(ResourceMeta.hrefTag);
		
		if (!Boolean.TRUE.equals(UrlValidator.getInstance().isValid(resourceHref))) {
			throw new BadRequestException(String.format("Passed URL for the %s resource is not a valid URL.", resourceClass.getSimpleName()));
		}
		
		final String expectedBasePath = trimPath(uriInfo.getBaseUri().toString());
		
		if (resourceHref.indexOf(expectedBasePath) != 0) {
			throw new BadRequestException(String.format("Passed URL '%s' for the %s resource contains incorrect base path, expected '%s'", resourceHref,
					resourceClass.getSimpleName(), expectedBasePath));
		}
		
		final String providedPath = resourceHref.substring(expectedBasePath.length());
		final String[] subPathComponents = trimPath(providedPath).split(ResourceMeta.PATH_SEPARATOR);
		
		if (subPathComponents.length < 2) {
			throw new BadRequestException(String.format("Passed URL for the %s resource does not contain a valid resource identifier.",
					resourceClass.getSimpleName()));
		}
		
		final String resourcePath = subPathComponents[0];
		final String resourceId = subPathComponents[1];
		final String expectedResourcePath = trimPath(ResourceMapper.getPathForResourceClass(resourceClass));
		
		if (!expectedResourcePath.equals(resourcePath)) {
			throw new BadRequestException(String.format("Passed URL for the %s resource contains path to a different resource, expected '%s' received '%s'",
					resourceClass.getSimpleName(), expectedResourcePath, resourcePath));
		}
		
		if (subPathComponents.length > 2) {
			throw new BadRequestException(String.format("Passed URL for the %s resource contains more than one identifier: '%s'",
					resourceClass.getSimpleName(), providedPath));
		}
		
		return resourceId;
	}
	
	public static String trimPath(String path) {
		if (!StringUtils.hasLength(path)) {
			return path;
		}
		
		path = StringUtils.trimWhitespace(path);
		
		if (path.startsWith(ResourceMeta.PATH_SEPARATOR)) {
			path = path.substring(1, path.length());
		}
		
		if (path.endsWith(ResourceMeta.PATH_SEPARATOR)) {
			path = path.substring(0, path.length() - 1);
		}
		
		return path;
	}
}
