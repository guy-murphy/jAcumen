package acumen.map.model;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 12:13:43
 */
public interface IScopedData {
    public Language getLanguage ();
    public void setLanguage (Language language);
    public String getScope ();
    public void setScope (String scope);
}
