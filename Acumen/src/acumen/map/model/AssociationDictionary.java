package acumen.map.model;

import acumen.util.Is;

import java.util.UUID;
import java.util.Arrays;
import java.io.IOException;

import acumen.data.xml.XmlWriter;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 12:00:34
 */
public class AssociationDictionary extends NodeDictionaryBase<IAssociation> implements IAssociationDictionary {

	private static final long serialVersionUID = -5417482837543529347L;

	public AssociationDictionary(IAssociationDictionary associations) {
        super(associations);
    }

    public AssociationDictionary(INode parent) {
        super(parent);
    }

    public IAssociation[] getSorted () {
        IAssociation[] associations = new IAssociation[this.size()];
        this.values().toArray(associations);
        Arrays.sort(associations, AssociationTypeRoleComparator.getInstance());
        return associations;
    }

    public void importAssociations (IAssociationDictionary associations) {
        for (IAssociation association: associations.values()) {
            this.add(association); 
        }
    }

    public IAssociation setAssociation (String reference, String type, String role, String scope) {
        IAssociation association = new Association((ITopic)this.getParent(), UUID.randomUUID().toString(), reference, type, role, scope);
        this.add(association);
        return association;
    }

    public IAssociation setAssociation (String reference, String type, String role) {
        String scope = this.getParent().getCurrentScope();
        return this.setAssociation(reference, type, role, scope);
    }

    public IAssociation find (String reference, String type, String role) {
        IAssociation association = null;
        for (IAssociation item: this.values()) {
            if (item.getReference().equals(reference) && item.getType().equals(type) && item.getRole().equals(role)) {
                association = item;
                break;
            }
        }
        return association;
    }

    public void filterByScope () {
        this.filterByScope(null);
    }

    public void filterByScope (String scope) {
        String scopeToUse = Is.NullOrEmpty(scope) ? this.getParent().getCurrentScope() : scope;
        AssociationDictionary reference = new AssociationDictionary(this);
        for (IAssociation item: reference.values()) {
            if (!item.getScope().equals(scopeToUse)) {
                this.remove(item.getId());
            }
        }
    }

    public void toXml (XmlWriter xml) throws IOException {
        super.toXml(xml, "associations");
    }

}
