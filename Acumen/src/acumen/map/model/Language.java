package acumen.map.model;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:15:05
 */
public enum Language {
    None(-1),
    Any(0),
    German(7),
    English(9),
	Spanish(10),
	French(12),
    Italian(16),
	Dutch(19),
	Norwegian(20),
	Russian(25),
	Sweedish(29);

	public static Language getLanguage(int value) throws IllegalArgumentException {
		for (Language language : Language.values()) {
			if (language.getValue() == value) {
				return language;
			}
		}
		throw new IllegalArgumentException("There is no enum matching the argument you provided.");
	}
	
	public static Language parseLanguage (String language) {
		if (language.equalsIgnoreCase("any")) {
			return Language.Any;
		} else if (language.equalsIgnoreCase("german")) {
			return Language.German;
		} else if (language.equalsIgnoreCase("English")) {
			return Language.English;
		} else if (language.equalsIgnoreCase("Spanish")) {
			return Language.Spanish;
		} else if (language.equalsIgnoreCase("French")) {
			return Language.French;
		} else if (language.equalsIgnoreCase("Italian")) {
			return Language.Italian;
		} else if (language.equalsIgnoreCase("Dutch")) {
			return Language.Dutch;
		} else if (language.equalsIgnoreCase("Norwegian")) {
			return Language.Norwegian;
		} else if (language.equalsIgnoreCase("Russian")) {
			return Language.Russian;
		} else if (language.equalsIgnoreCase("Sweedish")) {
			return Language.Sweedish;
		}
		return Language.None;
	}
	
	public static Language parseLanguageCode (String language) {
		if (language.equalsIgnoreCase("any")) {
			return Language.Any;
		} else if (language.equalsIgnoreCase("de")) {
			return Language.German;
		} else if (language.equalsIgnoreCase("en")) {
			return Language.English;
		} else if (language.equalsIgnoreCase("es")) {
			return Language.Spanish;
		} else if (language.equalsIgnoreCase("fr")) {
			return Language.French;
		} else if (language.equalsIgnoreCase("it")) {
			return Language.Italian;
		} else if (language.equalsIgnoreCase("nl")) {
			return Language.Dutch;
		} else if (language.equalsIgnoreCase("no")) {
			return Language.Norwegian;
		} else if (language.equalsIgnoreCase("ru")) {
			return Language.Russian;
		} else if (language.equalsIgnoreCase("se")) {
			return Language.Sweedish;
		}
		return Language.None;
	}

	private final int _value;

	Language(int value) {
		_value = value;
	}

	public int getValue() {
		return _value;
	}

}
