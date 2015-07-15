package me.stronglift.api.error;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.stronglift.api.util.ClassUtils;
import me.stronglift.api.util.OrderedProperties;
import me.stronglift.api.util.StringUtils;

/**
 * Exception mapper is responsible to map any exception that occurs into {@link ApiError}.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {
	
	/**
	 * The default name of the exception attribute: "exception".
	 */
	public static final String API_ERRORS_FILE = "apiErrors.properties";
	
	private static final String INFO_URL_KEY = "infoUrl";
	private static final String DEV_MESSAGE_KEY = "devMessage";
	private static final String MESSAGE_KEY = "message";
	private static final String ERROR_CODE_KEY = "errorCode";
	private static final String HTTP_STATUS_KEY = "httpStatus";
	public static final String EXCEPTION_CONFIG_DELIMITER = "|";
	
	private static final Logger log = LoggerFactory.getLogger(ApiExceptionMapper.class);
	
	private Map<String, ApiError> exceptionMappings = Collections.emptyMap();
	
	public ApiExceptionMapper() {
		OrderedProperties props = new OrderedProperties();
		props.load(ClassUtils.getResourceAsStream(API_ERRORS_FILE));
		this.exceptionMappings = ApiExceptionMapper.toApiErrors(props);
	}
	
	@Override
	public Response toResponse(Throwable t) {
		log.error(t.getLocalizedMessage(), t);
		ApiError error = getApiError(t);
		log.debug("Mapping '" + t.getClass().getSimpleName() + "' to API error '" + error.toString() + "'");
		return Response.status(Response.Status.fromStatusCode(error.getHttpStatus().getCode())).type(MediaType.APPLICATION_JSON_TYPE).entity(error.toMap())
				.build();
	}
	
	private ApiError getApiError(Throwable t) {
		
		ApiError template = getApiErrorTemplate(t);
		if (template == null) {
			return null;
		}
		
		ApiError.Builder builder = new ApiError.Builder();
		builder.setHttpStatus(template.getHttpStatus());
		builder.setErrorCode(template.getCode());
		builder.setMoreInfoUrl(template.getMoreInfoUrl());
		builder.setThrowable(t);
		
		builder.setMessage(template.getMessage() != null ? template.getMessage() : getMessage(t));
		builder.setDeveloperMessage(template.getDeveloperMessage() != null ? template.getDeveloperMessage() : getMessage(t));
		
		return builder.build();
	}
	
	/**
	 * Returns the deeps cause message.
	 * 
	 * @param t
	 * @return
	 */
	protected String getMessage(Throwable t) {
		
		String message = null;
		
		while (t != null && t.getMessage() != null) {
			message = t.getMessage();
			t = t.getCause();
		}
		
		return message;
	}
	
	private ApiError getApiErrorTemplate(Throwable t) {
		Map<String, ApiError> mappings = this.exceptionMappings;
		if (mappings == null || mappings.isEmpty()) {
			return null;
		}
		ApiError template = null;
		String dominantMapping = null;
		int deepest = Integer.MAX_VALUE;
		for (Map.Entry<String, ApiError> entry : mappings.entrySet()) {
			String key = entry.getKey();
			int depth = getDepth(key, t);
			if (depth >= 0 && depth < deepest) {
				deepest = depth;
				dominantMapping = key;
				template = entry.getValue();
			}
		}
		if (template != null && log.isDebugEnabled()) {
			log.debug("Resolving to ApiError template '" + template + "' for exception of type [" + t.getClass().getName() + "], based on exception mapping ["
					+ dominantMapping + "]");
		}
		return template;
	}
	
	/**
	 * Return the depth to the superclass matching.
	 * <p>
	 * 0 means it matches exactly. Returns -1 if there's no match. Otherwise, returns depth. Lowest depth wins.
	 */
	protected int getDepth(String exceptionMapping, Throwable t) {
		return getDepth(exceptionMapping, t.getClass(), 0);
	}
	
	private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
		if (exceptionClass.getSimpleName().equalsIgnoreCase(exceptionMapping)) {
			// Found it!
			return depth;
		}
		// If we've gone as far as we can go and haven't found it...
		if (exceptionClass.equals(Throwable.class)) {
			return -1;
		}
		return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
	}
	
	private static int getRequiredInt(String key, String value) {
		try {
			int anInt = Integer.valueOf(value);
			return Math.max(-1, anInt);
		} catch (NumberFormatException e) {
			String msg = "Mapping element '" + key + "' requires an integer value. The value specified: " + value;
			throw new IllegalArgumentException(msg, e);
		}
	}
	
	private static Map<String, ApiError> toApiErrors(Map<String, String> errorMap) {
		
		if (errorMap == null || errorMap.isEmpty()) {
			return Collections.emptyMap();
		}
		
		Map<String, ApiError> map = new LinkedHashMap<>(errorMap.size());
		
		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ApiError template = toApiError(key, value);
			log.debug("Creating API exception mapping for " + key + " Created mapping: " + value);
			map.put(key, template);
		}
		
		return map;
	}
	
	/**
	 * @param exceptionConfig
	 * @return
	 */
	/**
	 * @param exceptionConfig
	 * @return
	 */
	private static ApiError toApiError(final String exceptionClass, final String exceptionConfig) {
		
		String[] values = StringUtils.delimitedListToStringArray(exceptionConfig, EXCEPTION_CONFIG_DELIMITER);
		
		if (values == null || values.length == 0) {
			throw new IllegalStateException("Invalid exception config vaule. Exception names must map to a string configuration.");
		}
		
		ApiError.Builder apiErrorbuilder = new ApiError.Builder();
		
		for (String value : values) {
			
			String trimmedVal = StringUtils.trimWhitespace(value);
			
			// check to see if the value is an explicitly named key/value pair:
			String[] pair = StringUtils.split(trimmedVal, "=");
			if (pair != null) {
				
				String pairKey = StringUtils.trimWhitespace(pair[0]);
				if (!StringUtils.hasText(pairKey)) {
					pairKey = null;
				}
				String pairValue = StringUtils.trimWhitespace(pair[1]);
				if (!StringUtils.hasText(pairValue)) {
					pairValue = null;
				}
				if (HTTP_STATUS_KEY.equalsIgnoreCase(pairKey)) {
					int statusCode = getRequiredInt(pairKey, pairValue);
					apiErrorbuilder.setHttpStatus(statusCode);
				} else if (ERROR_CODE_KEY.equalsIgnoreCase(pairKey)) {
					int code = getRequiredInt(pairKey, pairValue);
					apiErrorbuilder.setErrorCode(code);
				} else if (MESSAGE_KEY.equalsIgnoreCase(pairKey)) {
					apiErrorbuilder.setMessage(pairValue);
				} else if (DEV_MESSAGE_KEY.equalsIgnoreCase(pairKey)) {
					apiErrorbuilder.setDeveloperMessage(pairValue);
				} else if (INFO_URL_KEY.equalsIgnoreCase(pairKey)) {
					apiErrorbuilder.setMoreInfoUrl(pairValue);
				} else {
					throw new IllegalArgumentException("Ivalid key '" + pairKey + "' in mapping for exception '" + exceptionClass + "'");
				}
			} else {
				
				throw new IllegalArgumentException("The symbol '=' is missing for the key/value pair '" + trimmedVal + "' in mapping for exception '"
						+ exceptionClass + "'");
			}
		}
		
		ApiError apiError;
		
		try {
			apiError = apiErrorbuilder.build();
		} catch (Exception e) {
			throw new IllegalArgumentException("API error mapping cannot be created for '" + exceptionClass + "', reason: " + e.getMessage());
		}
		
		return apiError;
	}
}
