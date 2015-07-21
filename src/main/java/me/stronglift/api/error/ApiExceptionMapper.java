package me.stronglift.api.error;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import me.stronglift.api.util.ClassUtils;
import me.stronglift.api.util.OrderedProperties;
import me.stronglift.api.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ApiExceptionMapper je odgovaran da mapira svaki exception u {@link ApiError}.
 * 
 * @author Dusan Eremic
 */
@Provider
public class ApiExceptionMapper implements ExceptionMapper<Throwable> {
	
	/**
	 * Ime properties fajla sa definisanim mapiranjem za exceptions.
	 */
	public static final String API_ERRORS_FILE = "apiErrors.properties";
	
	// Ključevi u properties fajlu
	private static final String INFO_URL_KEY = "infoUrl";
	private static final String DEV_MESSAGE_KEY = "devMessage";
	private static final String MESSAGE_KEY = "message";
	private static final String ERROR_CODE_KEY = "errorCode";
	private static final String HTTP_STATUS_KEY = "httpStatus";
	public static final String EXCEPTION_CONFIG_DELIMITER = "|";
	
	/** Logger */
	private static final Logger log = LoggerFactory
			.getLogger(ApiExceptionMapper.class);
	
	/** Mapiranja Exception:ApiError */
	private Map<String, ApiError> exceptionMappings = Collections.emptyMap();
	
	/**
	 * Constructor
	 */
	public ApiExceptionMapper() {
		OrderedProperties props = new OrderedProperties();
		props.load(ClassUtils.getResourceAsStream(API_ERRORS_FILE));
		this.exceptionMappings = ApiExceptionMapper.toApiErrors(props);
	}
	
	/**
	 * Konevrtuje Throwable u HTTP response.
	 */
	@Override
	public Response toResponse(Throwable t) {
		ApiError error = getApiError(t);
		log.debug("Mapping '" + t.getClass().getSimpleName()
				+ "' to API error '" + error.toString() + "'");
		return Response
				.status(Response.Status.fromStatusCode(error.getHttpStatus()
						.getCode())).type(MediaType.APPLICATION_JSON_TYPE)
				.entity(error.toMap()).build();
	}
	
	/**
	 * Kreira {@link ApiError} za dati Throwable na osnovu templejta.
	 * 
	 * @param t Throwable
	 * @return ApiError
	 */
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
		
		builder.setMessage(template.getMessage() != null ? template
				.getMessage() : getMessage(t));
		builder.setDeveloperMessage(template.getDeveloperMessage() != null ? template
				.getDeveloperMessage() : getMessage(t));
		
		return builder.build();
	}
	
	/**
	 * Vraća "najdublju" poruku o grešci.
	 * 
	 * @param t Throwable
	 */
	protected String getMessage(Throwable t) {
		
		String message = null;
		
		while (t != null && t.getMessage() != null) {
			message = t.getMessage();
			t = t.getCause();
		}
		
		return message;
	}
	
	/**
	 * Vraća ApiError templejt za Throwable
	 * 
	 * @param t Throwable
	 * @return ApiError
	 */
	private ApiError getApiErrorTemplate(Throwable t) {
		Map<String, ApiError> mappings = this.exceptionMappings;
		
		if (mappings == null || mappings.isEmpty()) {
			return null;
		}
		ApiError template = null;
		String matched = null;
		int deepest = Integer.MAX_VALUE;
		
		for (Map.Entry<String, ApiError> entry : mappings.entrySet()) {
			String key = entry.getKey();
			int depth = getDepth(key, t.getClass(), 0);
			if (depth >= 0 && depth < deepest) {
				deepest = depth;
				matched = key;
				template = entry.getValue();
			}
		}
		
		if (template != null) {
			log.debug("Resolving to ApiError template '" + template
					+ "' for exception of type [" + t.getClass().getName()
					+ "], based on exception mapping [" + matched + "]");
		}
		
		return template;
	}
	
	/**
	 * Vraća dubinu exceptionMapping u prosleđenom Throwable
	 * 
	 * @param exceptionMapping Simple class name greške čija dubina se traži.
	 * @param t Greška u kojoj se dubina traži.
	 * @return pronađenu dubinu ili 0 ako je exceptionMapping == Throwable ili
	 *         -1 ako ne postoji match
	 */
	protected int getDepth(String exceptionMapping, Throwable t) {
		return getDepth(exceptionMapping, t.getClass(), 0);
	}
	
	/**
	 * Vraća dubinu exceptionMapping u prosleđenom Throwable
	 * 
	 * @param exceptionMapping Simple class name greške čija dubina se traži.
	 * @param t Greška u kojoj se dubina traži.
	 * @depth Početna dubina.
	 * @return pronađenu dubinu ili 0 ako je exceptionMapping == Throwable ili
	 *         -1 ako ne postoji match
	 */
	private int getDepth(String exceptionMapping, Class<?> exceptionClass,
			int depth) {
		
		if (exceptionClass.getSimpleName().equalsIgnoreCase(exceptionMapping)) {
			return depth;
		}
		
		if (exceptionClass.equals(Throwable.class)) {
			return -1;
		}
		
		return getDepth(exceptionMapping, exceptionClass.getSuperclass(),
				depth + 1);
	}
	
	/**
	 * Metoda pokušava da konvertuje string iz properties fajl u integer.
	 * 
	 * @param key Key iz porperties fajla.
	 * @param value String vrednost iz properties fajla.
	 * @return Integer vrednost
	 */
	private static int getRequiredInt(String key, String value) {
		try {
			int anInt = Integer.valueOf(value);
			return Math.max(-1, anInt);
		} catch (NumberFormatException e) {
			String msg = "Mapping element '" + key
					+ "' requires an integer value. The value specified: "
					+ value;
			throw new IllegalArgumentException(msg, e);
		}
	}
	
	/**
	 * Metoda kovertuje mapu iz učitanu properties fajla u mapu {@link ApiError}
	 * sa ključem iz prop fajla.
	 * 
	 * @param errorMap Učitani properties fajl.
	 */
	private static Map<String, ApiError> toApiErrors(
			Map<String, String> errorMap) {
		
		if (errorMap == null || errorMap.isEmpty()) {
			return Collections.emptyMap();
		}
		
		Map<String, ApiError> map = new LinkedHashMap<>(errorMap.size());
		
		for (Map.Entry<String, String> entry : errorMap.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			ApiError template = toApiError(key, value);
			log.debug("Creating API exception mapping for " + key
					+ " Created mapping: " + value);
			map.put(key, template);
		}
		
		return map;
	}
	
	/**
	 * Konvertuje liniju iz properties fajla u {@link ApiError} templejt.
	 * 
	 * @param exceptionClass Key iz properties fajla (exception simple class
	 *            name).
	 * @param exceptionConfigLine Linija iz properties fajla.
	 * @return ApiError
	 */
	private static ApiError toApiError(final String exceptionClass,
			final String exceptionConfigLine) {
		
		String[] exceptionConfig = StringUtils.delimitedListToStringArray(
				exceptionConfigLine, EXCEPTION_CONFIG_DELIMITER);
		
		if (exceptionConfig == null || exceptionConfig.length == 0) {
			throw new IllegalStateException("Invalid error config vaule.");
		}
		
		ApiError.Builder apiErrorbuilder = new ApiError.Builder();
		
		for (String oneExceptionConfig : exceptionConfig) {
			
			String trimmedVal = StringUtils.trimWhitespace(oneExceptionConfig);
			
			String[] keyValue = StringUtils.split(trimmedVal, "=");
			if (keyValue != null) {
				
				String key = StringUtils.trimWhitespace(keyValue[0]);
				if (!StringUtils.hasText(key)) {
					key = null;
				}
				String pairValue = StringUtils.trimWhitespace(keyValue[1]);
				if (!StringUtils.hasText(pairValue)) {
					pairValue = null;
				}
				if (HTTP_STATUS_KEY.equalsIgnoreCase(key)) {
					int statusCode = getRequiredInt(key, pairValue);
					apiErrorbuilder.setHttpStatus(statusCode);
				} else if (ERROR_CODE_KEY.equalsIgnoreCase(key)) {
					int code = getRequiredInt(key, pairValue);
					apiErrorbuilder.setErrorCode(code);
				} else if (MESSAGE_KEY.equalsIgnoreCase(key)) {
					apiErrorbuilder.setMessage(pairValue);
				} else if (DEV_MESSAGE_KEY.equalsIgnoreCase(key)) {
					apiErrorbuilder.setDeveloperMessage(pairValue);
				} else if (INFO_URL_KEY.equalsIgnoreCase(key)) {
					apiErrorbuilder.setMoreInfoUrl(pairValue);
				} else {
					throw new IllegalArgumentException("Ivalid key '" + key
							+ "' in mapping for exception '" + exceptionClass
							+ "'");
				}
			} else {
				
				throw new IllegalArgumentException(
						"The symbol '=' is missing for the key/value pair '"
								+ trimmedVal + "' in mapping for exception '"
								+ exceptionClass + "'");
			}
		}
		
		ApiError apiError;
		
		try {
			apiError = apiErrorbuilder.build();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Error mapping cannot be created for '" + exceptionClass
							+ "', cause: " + e.getMessage());
		}
		
		return apiError;
	}
}
