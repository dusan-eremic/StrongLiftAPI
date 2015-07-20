package me.stronglift.api.controller.resource;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.CollectionReference;
import me.stronglift.api.model.EntityReference;
import me.stronglift.api.util.Convert;

import com.fasterxml.jackson.annotation.JsonValue;

public class ResourceLink {
	
	private final URI uri;
	private final LinkType type;
	
	private ResourceLink(URI uri, LinkType linkType) {
		this.uri = uri;
		this.type = linkType;
	}
	
	public URI getUri() {
		return uri;
	}
	
	public LinkType getType() {
		return type;
	}
	
	public static ResourceLink link(URI uri, LinkType type) {
		return new ResourceLink(uri, type);
	}
	
	public static <T extends BaseEntity<T>> ResourceLink link(URI absoultePath, LinkType type, T entity) {
		
		if (entity.getId() == null) {
			throw new IllegalArgumentException("Entity ID cannot be null!");
		}
		
		if (absoultePath.toString().contains(entity.getId())) {
			return new ResourceLink(absoultePath, type);
		} else {
			return new ResourceLink(UriBuilder.fromUri(absoultePath).path(entity.getId()).build(), type);
		}
	}
	
	public static <T extends BaseEntity<T>> ResourceLink link(URI baseUri, LinkType type, final EntityReference<T> reference) {
		return new ResourceLink(//
				UriBuilder.fromUri(baseUri) //
						.path(ResourceMapper.getPathForResourceClass(reference.getEntityClass())) //
						.path(Convert.toString(reference.getId())) //
						.build() //
				, type);
	}
	
	public static <E extends BaseEntity<E>, P extends BaseEntity<P>> ResourceLink link(URI baseUri, LinkType type,
			final CollectionReference<E, P> collectionReference) {
		
		if (!collectionReference.isValid()) {
			throw new RuntimeException("Invalid parent reference for the collection reference " + collectionReference.toString());
		}
		
		return new ResourceLink( //
				UriBuilder.fromUri(baseUri) //
						.path(ResourceMapper.getPathForResourceClass(collectionReference.getCollectionClass())) //
						.build() //
				, type);
	}
	
	public enum LinkType {
		
		SELF, INSTANCE, COLLECTION;
		
		@Override
		@JsonValue
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
}
