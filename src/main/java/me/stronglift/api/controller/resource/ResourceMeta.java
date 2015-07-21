package me.stronglift.api.controller.resource;

import java.util.LinkedHashMap;

/**
 * Meta podaci sa resurs. Ovo je osnovna klasa za {@link Resource} klasu.
 * 
 * @author Dušan Eremić
 *
 */
public class ResourceMeta extends LinkedHashMap<String, Object> {
	
	private static final long serialVersionUID = 4699384935569512738L;
	protected static final String PATH_SEPARATOR = "/";
	
	// JSON tagovi za meta data sekciju resursa
	private static final String metaTag = "meta";
	protected static final String hrefTag = "href";
	private static final String typeTag = "type";
	protected static final String lookupIdTag = "id";
	
	/**
	 * Constructor #01
	 */
	protected ResourceMeta() {
		put(metaTag, new LinkedHashMap<String, Object>());
	}
	
	/**
	 * Constructor #02
	 * 
	 * @param link {@link ResourceLink}
	 */
	@SuppressWarnings("unchecked")
	protected ResourceMeta(final ResourceLink link) {
		this();
		if (link != null) {
			((LinkedHashMap<String, Object>) get(metaTag)).put(hrefTag,
					link.getUri());
			((LinkedHashMap<String, Object>) get(metaTag)).put(typeTag,
					link.getType());
		} else {
			remove(metaTag);
		}
	}
}
