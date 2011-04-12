package acumen.map.model;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 13:36:11

 */
public interface IAssociation extends INode {
    String getSource ();
    void setSource (String source);
    String getReference ();
    void setReference (String reference);
    String getType ();
    void setType (String type);
    String getRole ();
    void setRole (String role);
    String getScope ();
    void setScope (String scope);
    ITopic getResolvedTopic ();
    void setResolvedTopic (ITopic topic);
    String getOrdinal ();
    void setOrdinal (String ordinal);
    String getCurrentScope ();
    void setCurrentScope (String scope);
}
