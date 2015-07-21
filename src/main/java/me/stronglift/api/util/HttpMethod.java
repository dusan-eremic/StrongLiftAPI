package me.stronglift.api.util;

/**
 * HTTP methods constants
 * 
 * @author Dusan Eremic
 *
 */
public class HttpMethod {
	
	public static final HttpMethod DELETE = new HttpMethod("DELETE");
	public static final HttpMethod HEAD = new HttpMethod("HEAD");
	public static final HttpMethod GET = new HttpMethod("GET");
	public static final HttpMethod OPTIONS = new HttpMethod("OPTIONS");
	public static final HttpMethod POST = new HttpMethod("POST");
	public static final HttpMethod PUT = new HttpMethod("PUT");
	public static final HttpMethod TRACE = new HttpMethod("TRACE");
	
	private final String methodName;
	
	public HttpMethod(String name) {
		methodName = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj == null || !(obj instanceof String)) {
			return false;
		}
		
		return this.methodName.equalsIgnoreCase((String) obj);
	}
}
