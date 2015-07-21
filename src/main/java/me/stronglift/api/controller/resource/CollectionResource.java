package me.stronglift.api.controller.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.model.BaseEntity;

/**
 * Generički collection resource će biti korišćen za pakovanje kolekcije
 * {@link BaseEntity} objekata u kolekiciju {@link Resource} objekata.
 * 
 * @author Dusan Eremic
 *
 */
public class CollectionResource<T extends BaseEntity<T>> extends ResourceMeta {
	
	private static final long serialVersionUID = -5345313869770050860L;
	
	/**
	 * Tag koji će biti korišćen u JSON poruci za resource array.
	 */
	private static final String itemsTag = "items";
	
	/**
	 * Constructor
	 * 
	 * @param uriInfo UriInfo instanca prosleđena iz controller-a.
	 * @param collection Pripremljena kolekcija za slanje klijentu.
	 */
	public CollectionResource(UriInfo uriInfo, List<T> collection) {
		
		super(ResourceLink.link(uriInfo.getAbsolutePath(), LinkType.SELF));
		
		Collection<ResourceMeta> resourceCollection = new ArrayList<>(
				collection.size());
		
		for (T entity : collection) {
			resourceCollection.add(new Resource<>(uriInfo, LinkType.INSTANCE,
					entity));
		}
		
		put(itemsTag, resourceCollection);
	}
	
}
