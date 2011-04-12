package acumen.map.model;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 17:17:29
 */
public interface ITopic extends INode {
    IOccurenceList getOccurences ();

    IAssociationDictionary getAssociations ();

    Language[] getLanguagesUsed ();

    String[] getScopesUsed ();

    void importTopic (Topic topic);

    ITopic cloneAs (String id);

    void addAssociation (IAssociation association);

    void addOccurence (IOccurence occurence);

    IAssociation createAssociation (String id, String reference, String type, String role, String scope, String label);

    IAssociation createAssociation (String reference, String type, String role, String scope, String label);

    IAssociation createAssociation (String reference, String type, String role, String scope);

    IAssociation createAssociation (String reference, String type, String role);

    IOccurence createOccurence (String reference, String behaviour, Language language, String scope, String role);

    IOccurence createOccurence (String reference, String behaviour, Language language);

    IOccurence createOccurence (String reference, String behaviour, String scope);

    IOccurence createOccurence (String reference, String behaviour);

    String generateETag (Language language, String scope);
}
