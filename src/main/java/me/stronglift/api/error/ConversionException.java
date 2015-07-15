package me.stronglift.api.error;

/**
 * <p>
 * A <strong>ConversionException</strong> indicates that a call to <code>@Convert.to()</code> has failed to complete successfully.
 *
 * @author Dusan Eremic
 */

public class ConversionException extends RuntimeException {
	
	private static final long serialVersionUID = -7089241511223407813L;
	
	/**
	 * The field that holds the value.
	 */
	private String field;
	
	/**
	 * Construct a new exception with the specified message.
	 *
	 * @param message The message describing this exception
	 */
	public ConversionException(String message) {
		super(message);
	}
	
	/**
	 * Construct a new exception with the specified message.
	 *
	 * @param field The field that holds the value.
	 * @param message The message describing this exception
	 */
	public ConversionException(String field, String message) {
		super(message);
	}
	
	/**
	 * Sets the field that holds the value (optional).
	 * 
	 * @param field
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	@Override
	public String getMessage() {
		if (field == null || field.isEmpty()) {
			return super.getMessage();
		} else {
			return "[Field: " + this.field + "]" + super.getMessage();
		}
	}
}
