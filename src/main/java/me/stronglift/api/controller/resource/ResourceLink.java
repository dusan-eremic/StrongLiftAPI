package me.stronglift.api.controller.resource;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import me.stronglift.api.model.BaseEntity;
import me.stronglift.api.model.CollectionReference;
import me.stronglift.api.model.EntityReference;
import me.stronglift.api.util.Convert;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Pomoćna klasa za generisanje resurs URI-ja sa pridruženim tipom. Koristi se u
 * {@link ResourceMeta}.
 * 
 * @author Dusan Eremic
 */
public class ResourceLink {
	
	/** Resurs URI */
	private final URI uri;
	/** URI type */
	private final LinkType type;
	
	/**
	 * Private constructor
	 * 
	 * @param uri Resurs URI
	 * @param linkType URI type
	 */
	private ResourceLink(URI uri, LinkType linkType) {
		this.uri = uri;
		this.type = linkType;
	}
	
	/**
	 * Vraća resurs URI
	 */
	public URI getUri() {
		return uri;
	}
	
	/**
	 * Vraća resurs URI type
	 */
	public LinkType getType() {
		return type;
	}
	
	/**
	 * Kreira resurs link na osnovu prosleđenog URI-ja i {@link LinkType} bez
	 * dodatnih modifikacija URI-ja.
	 * 
	 * @param uri Resurs URI.
	 * @param type URI tip.
	 * @return ResourceLink
	 */
	public static ResourceLink link(URI uri, LinkType type) {
		return new ResourceLink(uri, type);
	}
	
	/**
	 * Kreira resurs link za prosleđenu instancu model klase.
	 * 
	 * @param absoultePath Apsolutna putanja do resersa. Ukoliko ne sadrži
	 *            entity.id, id će biti dodat na kraju putanje.
	 * @param type {@link LinkType}
	 * @param entity Entitet za koji se kreira link.
	 * @return {@link ResourceLink}
	 */
	public static <T extends BaseEntity<T>> ResourceLink link(URI absoultePath,
			LinkType type, T entity) {
		
		if (entity.getId() == null) {
			throw new IllegalArgumentException("Entity ID cannot be null!");
		}
		
		if (absoultePath.toString().contains(entity.getId())) {
			return new ResourceLink(absoultePath, type);
		} else {
			return new ResourceLink(UriBuilder.fromUri(absoultePath)
					.path(entity.getId()).build(), type);
		}
	}
	
	/**
	 * Kreira resurs link za prosleđeni EntityReference.
	 * 
	 * @param baseUri Base URI.
	 * @param type {@link LinkType}
	 * @param reference {@link EntityReference}
	 * @return {@link ResourceLink}
	 */
	public static <T extends BaseEntity<T>> ResourceLink link(URI baseUri,
			LinkType type, final EntityReference<T> reference) {
		return new ResourceLink(//
				UriBuilder.fromUri(baseUri)
						//
						.path(ResourceMapper.getPathForResourceClass(reference
								.getEntityClass())) //
						.path(Convert.toString(reference.getId())) //
						.build() //
				, type);
	}
	
	/**
	 * Kreira resurs link za prosleđeni CollectionReference.
	 * 
	 * @param baseUri Base URI.
	 * @param type {@link LinkType}
	 * @param collectionReference {@link CollectionReference}
	 * @return {@link ResourceLink}
	 */
	public static <E extends BaseEntity<E>, P extends BaseEntity<P>> ResourceLink link(
			URI baseUri, LinkType type,
			final CollectionReference<E, P> collectionReference) {
		
		if (!collectionReference.isValid()) {
			throw new RuntimeException(
					"Invalid parent reference for the collection reference "
							+ collectionReference.toString());
		}
		
		return new ResourceLink( //
				UriBuilder.fromUri(baseUri)
						//
						.path(ResourceMapper
								.getPathForResourceClass(collectionReference
										.getCollectionClass())) //
						.build() //
				, type);
	}
	
	/**
	 * Tipovi resurs linka
	 * 
	 * @author Dusan Eremic
	 */
	public enum LinkType {
		
		/**
		 * Link na samog sebe.
		 */
		SELF,
		/**
		 * Link na instancu drugog resursa.
		 */
		INSTANCE,
		/**
		 * Link na kolekciju resursa.
		 */
		COLLECTION;
		
		@Override
		@JsonValue
		public String toString() {
			return super.toString().toLowerCase();
		}
	}
	
}
