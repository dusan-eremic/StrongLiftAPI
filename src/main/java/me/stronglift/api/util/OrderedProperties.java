package me.stronglift.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Replacement for the java.util.Properties class that retains the order in
 * which the properties are defined.
 */
public class OrderedProperties implements Map<String, String> {
	
	public static final String DEFAULT_CHARSET_NAME = "UTF-8";
	public static final String COMMENT_POUND = "#";
	public static final String COMMENT_SEMICOLON = ";";
	protected static final char ESCAPE_TOKEN = '\\';
	private final Map<String, String> props;
	
	/**
	 * Constructror
	 */
	public OrderedProperties() {
		this.props = new LinkedHashMap<>();
	}
	
	/**
	 * Loads the .properties backed by the given InputStream into this instance.
	 * This implementation will close the input stream after it has finished
	 * loading. It is expected that the stream's contents are UTF-8 encoded.
	 *
	 * @param is the {@code InputStream} from which to read the INI-formatted
	 *            text
	 */
	public void load(InputStream is) {
		String string;
		try {
			string = new Scanner(is, DEFAULT_CHARSET_NAME).useDelimiter("\\A")
					.next();
		} catch (NoSuchElementException nsee) {
			string = "";
		} finally {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
		
		Map<String, String> props = toMapProps(string);
		putAll(props);
	}
	
	private static boolean isContinued(String line) {
		if (!StringUtils.hasText(line)) {
			return false;
		}
		int length = line.length();
		// find the number of backslashes at the end of the line. If an even
		// number, the
		// backslashes are considered escaped. If an odd number, the line is
		// considered continued on the next line
		int backslashCount = 0;
		for (int i = length - 1; i > 0; i--) {
			if (line.charAt(i) == ESCAPE_TOKEN) {
				backslashCount++;
			} else {
				break;
			}
		}
		return backslashCount % 2 != 0;
	}
	
	private static boolean isKeyValueSeparatorChar(char c) {
		return Character.isWhitespace(c) || c == ':' || c == '=';
	}
	
	private static boolean isCharEscaped(CharSequence s, int index) {
		return index > 0 && s.charAt(index - 1) == ESCAPE_TOKEN;
	}
	
	private static String[] splitKeyValue(String keyValueLine) {
		String line = StringUtils.clean(keyValueLine);
		if (line == null) {
			return null;
		}
		StringBuilder keyBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();
		
		boolean buildingKey = true; // we'll build the value next:
		
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			
			if (buildingKey) {
				if (isKeyValueSeparatorChar(c) && !isCharEscaped(line, i)) {
					buildingKey = false;// now start building the value
				} else {
					keyBuffer.append(c);
				}
			} else {
				if (valueBuffer.length() == 0 && isKeyValueSeparatorChar(c)
						&& !isCharEscaped(line, i)) {
					// swallow the separator chars before we start building the
					// value
				} else {
					valueBuffer.append(c);
				}
			}
		}
		
		String key = StringUtils.clean(keyBuffer.toString());
		String value = StringUtils.clean(valueBuffer.toString());
		
		if (key == null || value == null) {
			String msg = "Line argument must contain a key and a value.  Only one string token was found.";
			throw new IllegalArgumentException(msg);
		}
		
		// log.trace("Discovered key/value pair: {}={}", key, value);
		
		return new String[] { key, value };
	}
	
	private static Map<String, String> toMapProps(String content) {
		Map<String, String> props = new LinkedHashMap<>();
		String line;
		StringBuilder lineBuffer = new StringBuilder();
		Scanner scanner = new Scanner(content);
		while (scanner.hasNextLine()) {
			
			line = StringUtils.clean(scanner.nextLine());
			
			if (line == null || line.startsWith(COMMENT_POUND)
					|| line.startsWith(COMMENT_SEMICOLON)) {
				// skip empty lines and comments:
				continue;
			}
			
			if (isContinued(line)) {
				// strip off the last continuation backslash:
				line = line.substring(0, line.length() - 1);
				lineBuffer.append(line);
				continue;
			} else {
				lineBuffer.append(line);
			}
			line = lineBuffer.toString();
			lineBuffer = new StringBuilder();
			String[] kvPair = splitKeyValue(line);
			props.put(kvPair[0], kvPair[1]);
		}
		
		scanner.close();
		
		return props;
	}
	
	public void clear() {
		this.props.clear();
	}
	
	public boolean containsKey(Object key) {
		return this.props.containsKey(key);
	}
	
	public boolean containsValue(Object value) {
		return this.props.containsValue(value);
	}
	
	public Set<Entry<String, String>> entrySet() {
		return this.props.entrySet();
	}
	
	public String get(Object key) {
		return this.props.get(key);
	}
	
	public boolean isEmpty() {
		return this.props.isEmpty();
	}
	
	public Set<String> keySet() {
		return this.props.keySet();
	}
	
	public String put(String key, String value) {
		return this.props.put(key, value);
	}
	
	public void putAll(Map<? extends String, ? extends String> m) {
		this.props.putAll(m);
	}
	
	public String remove(Object key) {
		return this.props.remove(key);
	}
	
	public int size() {
		return this.props.size();
	}
	
	public Collection<String> values() {
		return this.props.values();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OrderedProperties) {
			OrderedProperties other = (OrderedProperties) obj;
			return this.props.equals(other.props);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(this.props);
	}
	
}
