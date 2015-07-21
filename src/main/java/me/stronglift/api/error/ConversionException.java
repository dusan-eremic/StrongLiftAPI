package me.stronglift.api.error;

import me.stronglift.api.util.Convert;

/**
 * Greška prilikom konverzije tipova u {@link Convert} klasi.
 *
 * @author Dusan Eremic
 */

public class ConversionException extends RuntimeException {
	
	private static final long serialVersionUID = -7089241511223407813L;
	
	/**
	 * Naziv polja čija konverzija vrednosti nije uspela.
	 */
	private String field;
	
	/**
	 * Constructor #1
	 *
	 * @param message Poruka o grešci.
	 */
	public ConversionException(String message) {
		super(message);
	}
	
	/**
	 * Constructor #2
	 * 
	 * @param field Naziv polja čija konverzija vrednosti nije uspela.
	 * @param message Poruka o grešci.
	 */
	public ConversionException(String field, String message) {
		super(message);
	}
	
	/**
	 * Naziv polja čija konverzija vrednosti nije uspela.
	 */
	public void setField(String field) {
		this.field = field;
	}
	
	/**
	 * Vraća poruku o grešci koja uključuje naziv polja i razlog greške.
	 */
	@Override
	public String getMessage() {
		if (field == null || field.isEmpty()) {
			return super.getMessage();
		} else {
			return "[Field: " + this.field + "]" + super.getMessage();
		}
	}
}
