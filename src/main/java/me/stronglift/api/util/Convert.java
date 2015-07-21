package me.stronglift.api.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import me.stronglift.api.error.ConversionException;

/**
 * Data types converter
 * 
 * @author Dusan Eremic
 *
 */
public class Convert {
	
	/** UTC format vremena */
	public static final String timeFormat = "2007-12-03T10:15:30.000Z";
	
	/** Object to Instant */
	public static Instant toInstant(Object iso8601Time) {
		
		Instant time = null;
		
		if (iso8601Time == null) {
			return null;
		} else if (iso8601Time instanceof String) {
			if (!StringUtils.hasLength((String) iso8601Time)) {
				return null;
			} else {
				try {
					time = Instant.parse((String) iso8601Time);
				} catch (DateTimeParseException exception) {
					throw new ConversionException(
							String.format(
									"Passed date '%s' is not parsable UTC time format. Time to at least the seconds field is required in the format %s",
									iso8601Time, timeFormat));
				}
			}
		} else {
			throw new ConversionException(String.format(
					"Cannot convert %s to Instant", iso8601Time.getClass()
							.getSimpleName()));
		}
		
		return time;
	}
	
	/** Instant to String */
	public static String toString(Instant instant) {
		
		if (instant == null) {
			return null;
		}
		
		return DateTimeFormatter.ISO_INSTANT.format(instant);
	}
	
	/** Object to String */
	public static String toString(Object string) {
		
		if (string == null) {
			return null;
		} else if (string instanceof String) {
			return (String) string;
		} else {
			return String.valueOf(string);
		}
	}
	
	/** Double to String */
	public static String toString(Double number, int decimals) {
		
		if (number == null) {
			return null;
		}
		
		return new BigDecimal(number).setScale(decimals, RoundingMode.HALF_UP)
				.toString();
	}
	
	/** BigDecimal to String */
	public static String toString(BigDecimal number, int decimals) {
		
		if (number == null) {
			return null;
		}
		
		return number.setScale(decimals, RoundingMode.HALF_UP).toString();
	}
	
	/** Object to Integer */
	public static Integer toInt(Object integer) {
		
		final String errorMessage = "Cannot convert %s to Integer";
		
		if (integer == null) {
			return null;
		} else if (integer instanceof Integer) {
			return (Integer) integer;
		} else if (integer instanceof String) {
			if (!StringUtils.hasLength((String) integer)) {
				return null;
			} else {
				try {
					return Integer.valueOf((String) integer);
				} catch (NumberFormatException nfe) {
					throw new ConversionException(String.format(errorMessage,
							integer));
				}
			}
		} else {
			throw new ConversionException(String.format(errorMessage, integer
					.getClass().getSimpleName()));
		}
	}
	
	/** Object to Double */
	public static Double toDouble(Object number) {
		
		final String errorMessage = "Cannot convert %s to Double";
		
		if (number == null) {
			return null;
		} else if (number instanceof Double) {
			return (Double) number;
		} else if (number instanceof Integer) {
			return new Double((Integer) number);
		} else if (number instanceof String) {
			if (!StringUtils.hasLength((String) number)) {
				return null;
			} else {
				try {
					return Double.valueOf((String) number);
				} catch (NumberFormatException nfe) {
					throw new ConversionException(String.format(errorMessage,
							number));
				}
			}
		} else {
			throw new ConversionException(String.format(errorMessage, number
					.getClass().getSimpleName()));
		}
	}
	
	/** Object to BigDecimal */
	public static BigDecimal toBigDecimal(Object number) {
		
		final String errorMessage = "Cannot convert %s to BigDecimal";
		
		if (number == null) {
			return null;
		} else if (number instanceof BigDecimal) {
			return (BigDecimal) number;
		} else if (number instanceof Integer) {
			return new BigDecimal((Integer) number);
		} else if (number instanceof Double) {
			return new BigDecimal((Double) number);
		} else if (number instanceof String) {
			if (!StringUtils.hasLength((String) number)) {
				return null;
			} else {
				try {
					return new BigDecimal((String) number);
				} catch (NumberFormatException nfe) {
					throw new ConversionException(String.format(errorMessage,
							number));
				}
			}
		} else {
			throw new ConversionException(String.format(errorMessage, number
					.getClass().getSimpleName()));
		}
	}
	
	/** String to enum */
	public static <T> T toEnum(String name, Class<T> clazz) {
		for (T t : clazz.getEnumConstants()) {
			if (t.toString().equalsIgnoreCase(name)) {
				return t;
			}
		}
		return null;
	}
}
