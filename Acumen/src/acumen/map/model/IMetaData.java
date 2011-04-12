package acumen.map.model;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 12:26:12
 */
public interface IMetaData extends IScopedData {
    public String getParentId ();
    public void setParentId (String parentId);
    public String getName ();
    public void setName (String name);
    public String getValue ();
    public void setValue (String value);
}
