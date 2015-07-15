package me.stronglift.api.controller.resource;

import java.net.URI;
import java.util.LinkedHashMap;

import me.stronglift.api.controller.resource.ResourceLink.LinkType;

/**
 * Meta data for a resource - TODO JavaDoc
 * 
 * @author Dušan Eremić
 *
 */
public class ResourceMeta extends LinkedHashMap<String, Object> {
	
	private static final long serialVersionUID = 4699384935569512738L;
	protected static final String PATH_SEPARATOR = "/";
	private static final String metaTag = "meta";
	protected static final String hrefTag = "href";
	private static final String typeTag = "type";
	protected static final String lookupIdTag = "id";
	
	protected ResourceMeta() {
		put(metaTag, new LinkedHashMap<String, Object>());
	}
	
	protected ResourceMeta(final ResourceLink link) {
		this();
		if (link != null) {
			addToMeta(hrefTag, link.getUri());
			addToMeta(typeTag, link.getType());
		} else {
			remove(metaTag);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addToMeta(String fieldName, Object value) {
		((LinkedHashMap<String, Object>) get(metaTag)).put(fieldName, value);
	}
	
	@SuppressWarnings("unchecked")
	public URI getHref() {
		return (URI) ((LinkedHashMap<String, Object>) get(metaTag)).get(hrefTag);
	}
	
	@SuppressWarnings("unchecked")
	public LinkType getLinkType() {
		return (LinkType) ((LinkedHashMap<String, Object>) get(metaTag)).get(typeTag);
	}
	
}
