package acumen.util;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 12:14:16
 */
public class Assert {
    public static void HasValue (String string) {
        if (Is.NullOrEmpty(string)) {
            throw new IllegalArgumentException("There was no string value where one was expected.");
        }
    }

    public static void NotNull (Object item) {
        if (item == null) {
            throw new IllegalArgumentException("The item provided was null where this is not expected.");    
        }
    }
    
    public static void IsNull (Object item) {
    	if (item != null) {
    		throw new IllegalArgumentException("The item provided was expected to be null but is not.");
    	}
    }
    
    public static void NotNull (String label, Object item) {
        if (item == null) {
            throw new IllegalArgumentException(String.format("The item '%s' provided was null where this is not expected.", label));    
        }
    }
    
    public static void IsNull (String label, Object item) {
    	if (item != null) {
    		throw new IllegalArgumentException(String.format("The item '%s' provided was expected to be null but is not.", label));
    	}
    }
    
    public static void AreEqual (String name, Object obj1, Object obj2) {
		if (!obj1.equals(obj2)) {
			throw new IllegalArgumentException(String.format("The parmaeters for '%s' should be equal and are not.", name));
		}
	}

	public static void AreNotEqual (String name, Object obj1, Object obj2) {
		if (obj1.equals(obj2)) {
			throw new IllegalArgumentException(String.format("The parmaeters for '%s' should not be equal and are.", name));
		}
	}
}
