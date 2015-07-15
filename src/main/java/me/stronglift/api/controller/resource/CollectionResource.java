package me.stronglift.api.controller.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import me.stronglift.api.controller.resource.ResourceLink.LinkType;
import me.stronglift.api.entity.BaseEntity;

/**
 * Collection resource
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public class CollectionResource<T extends BaseEntity<T>> extends ResourceMeta {
	
	private static final long serialVersionUID = -5345313869770050860L;
	
	private static final String itemsTag = "items";
	
	public CollectionResource(UriInfo uriInfo, List<T> collection) {
		
		super(ResourceLink.link(uriInfo.getAbsolutePath(), LinkType.SELF));
		
		Collection<ResourceMeta> resourceCollection = new ArrayList<>(collection.size());
		
		for (T entity : collection) {
			resourceCollection.add(new Resource<>(uriInfo, LinkType.INSTANCE, entity));
		}
		
		put(itemsTag, resourceCollection);
	}
	
}
