package com.nedzhang.skunktool3;

public class SkunkUtil {

	private SkunkUtil() {

	}

	/***
	 * Parse a string to boolean. Null or empty string return false. y, yes,
	 * true return true
	 * 
	 * @param value
	 *            the String to parse
	 * @return boolean value
	 */
	public static boolean parseBoolean(final String value) {

		return value != null
				&& value.length() > 0
				&& ("y".equalsIgnoreCase(value)
						|| "yes".equalsIgnoreCase(value) || Boolean
							.valueOf(value));

	}
}
