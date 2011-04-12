package acumen.util;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 12:11:59
 */
public class Is {

    public static boolean NullOrEmpty (String string) {
        return string == null || string.length() == 0;
    }

    public static boolean NotNullOrEmpty (String string) {
    	return string != null && string.length() > 0;
    }

}
