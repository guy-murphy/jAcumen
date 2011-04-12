package acumen.map.model;


/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 10:47:24
 */
public interface IAssociationDictionary extends INodeDictionaryBase<IAssociation>, INodeChild {
    void importAssociations (IAssociationDictionary associations);
    IAssociation setAssociation (String reference, String type, String role, String scope);
    IAssociation setAssociation (String reference, String type, String role);
    IAssociation find (String reference, String type, String role);
    void filterByScope ();
    void filterByScope (String scope);
}
