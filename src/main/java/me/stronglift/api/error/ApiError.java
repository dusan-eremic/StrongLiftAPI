package me.stronglift.api.error;

import java.util.LinkedHashMap;
import java.util.Map;

import me.stronglift.api.util.HttpStatus;
import me.stronglift.api.util.ObjectUtils;

/**
 * A class that encaptualtes all API errors.
 * 
 * @author Dusan Eremic TODO - complete JavaDoc
 *
 */
public class ApiError {
	
	private static final String DEFAULT_MORE_INFO_URL = "mailto:api@stronglift.me";
	
	private final HttpStatus status;
	private final int code;
	private final String message;
	private final String developerMessage;
	private final String moreInfoUrl;
	private final Throwable throwable;
	
	private ApiError(HttpStatus status, int code, String message, String developerMessage, String moreInfoUrl, Throwable throwable) {
		if (status == null) {
			throw new NullPointerException("HttpStatus argument cannot be null.");
		}
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.moreInfoUrl = moreInfoUrl;
		this.throwable = throwable;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o instanceof ApiError) {
			ApiError re = (ApiError) o;
			return ObjectUtils.nullSafeEquals(getHttpStatus(), re.getHttpStatus()) && ObjectUtils.nullSafeEquals(getCode(), re.getCode())
					&& ObjectUtils.nullSafeEquals(getMessage(), re.getMessage()) && ObjectUtils.nullSafeEquals(getDeveloperMessage(), re.getDeveloperMessage())
					&& ObjectUtils.nullSafeEquals(getMoreInfoUrl(), re.getMoreInfoUrl()) && ObjectUtils.nullSafeEquals(getThrowable(), re.getThrowable());
			
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(getHttpStatus(), getCode(), getMessage(), getDeveloperMessage(), getMoreInfoUrl(), getThrowable());
	}
	
	public String toString() {
		return append(new StringBuilder(), getHttpStatus()).append(", message: ").append(getMessage()).toString();
	}
	
	private StringBuilder append(StringBuilder sb, HttpStatus status) {
		sb.append(status.getCode()).append(" (").append(status.getReason()).append(")");
		return sb;
	}
	
	private String toString(HttpStatus status) {
		return append(new StringBuilder(), status).toString();
	}
	
	public HttpStatus getHttpStatus() {
		return status;
	}
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getDeveloperMessage() {
		return developerMessage;
	}
	
	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	/**
	 * {@link ApiError} to {@link Map} of values.
	 * 
	 * @return
	 */
	public Map<String, ?> toMap() {
		final Map<String, Object> map = new LinkedHashMap<>();
		
		// 0. HTTP STATUS CODE
		map.put("httpStatusCode", getHttpStatus().getCode());
		
		// 1. ERROR CODE
		int code = getCode();
		if (code <= 0) {
			code = getHttpStatus().getCode();
		}
		map.put("errorCode", code);
		
		// 2. ERROR MESSAGE
		String message = getMessage();
		if (message == null) {
			message = toString(getHttpStatus());
		}
		map.put("errorMessage", message);
		
		// 3. DEVELOPER MESSAGE
		String developerMessage = getDeveloperMessage();
		if (developerMessage == null) {
			
			developerMessage = toString(getHttpStatus());
			
			if (getThrowable() != null) {
				developerMessage = developerMessage + ": " + getThrowable().getMessage();
			}
		}
		map.put("developerErrorMessage", developerMessage);
		
		// 4. MORE INFO
		String moreInfoUrl = getMoreInfoUrl();
		if (moreInfoUrl == null) {
			moreInfoUrl = DEFAULT_MORE_INFO_URL;
		}
		map.put("moreInfo", moreInfoUrl);
		
		return map;
	}
	
	public static class Builder {
		
		private HttpStatus httpStatus;
		private int errorCode;
		private String message;
		private String developerMessage;
		private String moreInfoUrl;
		private Throwable throwable;
		
		public Builder() {
		}
		
		public Builder setHttpStatus(int statusCode) {
			this.httpStatus = HttpStatus.valueOf(statusCode);
			return this;
		}
		
		public Builder setHttpStatus(HttpStatus status) {
			this.httpStatus = status;
			return this;
		}
		
		public Builder setErrorCode(int code) {
			this.errorCode = code;
			return this;
		}
		
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		
		public Builder setDeveloperMessage(String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}
		
		public Builder setMoreInfoUrl(String moreInfoUrl) {
			this.moreInfoUrl = moreInfoUrl;
			return this;
		}
		
		public Builder setThrowable(Throwable throwable) {
			this.throwable = throwable;
			return this;
		}
		
		public ApiError build() {
			return new ApiError(this.httpStatus, this.errorCode, this.message, this.developerMessage, this.moreInfoUrl, this.throwable);
		}
	}
}
