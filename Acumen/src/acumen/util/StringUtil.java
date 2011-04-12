package acumen.util;

import java.util.UUID;

public class StringUtil {
	public static String join(String[] parts, String delim) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < parts.length; i++) {
			if (i > 0) {
				builder.append(delim);
			}
			builder.append(parts[i]);
		}
		return builder.toString();
	}

	public static String trimEndsBy(String item, int numberOfCharacters) {
		return item.substring(numberOfCharacters, item.length()
				- numberOfCharacters);
	}

	public static String trimLeftBy(String item, int numberOfCharacters) {
		return item.substring(numberOfCharacters);
	}

	public static String trimRightBy(String item, int numberOfCharacters) {
		return item.substring(0, item.length() - numberOfCharacters);
	}

	/**
	 * This method ensures that the output String has only valid XML unicode
	 * characters as specified by the XML 1.0 standard. For reference, please
	 * see <a href="http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char">the
	 * standard</a>. This method will return an empty String if the input is
	 * null or empty.
	 * 
	 * @param in
	 *            The String whose non-valid characters we want to remove.
	 * @return The in String, stripped of non-valid characters.
	 */
	public static String stripNonValidXmlCharacters(String in) {
		StringBuffer out = new StringBuffer(); // Used to hold the output.
		char current; // Used to reference the current character.

		if (in == null || ("".equals(in)))
			return ""; // vacancy test.
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught
									// here; it should not happen.
			if (
					(current == 0x9) 
				|| 	(current == 0xA) 
				|| 	(current == 0xD)
				|| 	((current >= 0x20) && (current <= 0xD7FF))
				|| 	((current >= 0xE000) && (current <= 0xFFFD))
				|| 	((current >= 0x10000) && (current <= 0x10FFFF))
			)
			out.append(current);
		}
		return out.toString();
	}
	
	public static String generateUuid () {
		return UUID.randomUUID().toString();
	}

}
