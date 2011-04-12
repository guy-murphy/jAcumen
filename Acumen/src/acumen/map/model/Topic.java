package acumen.map.model;

import acumen.data.xml.XmlWriter;

import java.util.HashSet;
import java.util.UUID;
import java.io.IOException;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:12:39
 */
public class Topic extends NodeBase implements ITopic {

    private IOccurenceList _occurences;
    private IAssociationDictionary _associations;

    public Topic () {
        super();
    }

    public Topic (String id) {
        super(id);
    }

    public Topic (Topic topic) {
        super(topic);
        _associations = new AssociationDictionary(topic.getAssociations());
        _occurences = new OccurenceList(topic.getOccurences());
    }

    public Topic (INode parent, String id) {
        super(parent,id);
    }

    public IOccurenceList getOccurences () {
        if (_occurences == null) {
            _occurences = new OccurenceList(this);
        }
        return _occurences;
    }

    public IAssociationDictionary getAssociations () {
        if (_associations == null) {
            _associations = new AssociationDictionary(this);
        }
        return _associations;
    }

    public Language[] getLanguagesUsed () {
        HashSet<Language> languages = new HashSet<Language>();
        for (IMetaData m: this.getMeta()) {
            languages.add(m.getLanguage());
        }
        for (IOccurence o: this.getOccurences()) {
            languages.add(o.getLanguage());
        }
        Language[] result = new Language[languages.size()];
        languages.toArray(result);
        return result;
    }

    public String[] getScopesUsed () {
        HashSet<String> scopes = new HashSet<String>();
        for (IMetaData m: this.getMeta()) {
            scopes.add(m.getScope());
        }
        for (IOccurence o: this.getOccurences()) {
            scopes.add(o.getScope());
        }
        String[] result = new String[scopes.size()];
        scopes.toArray(result);
        return result;
    }

    public void importTopic (Topic topic) {
        this.getMeta().importMetaData(topic.getMeta());
        this.getOccurences().importOccurences(topic.getOccurences());
        this.getAssociations().importAssociations(topic.getAssociations());
    }

    public ITopic cloneAs (String id) {
        ITopic copy = new Topic(id);
        copy.importTopic(this);
        return copy;
    }

    public void addAssociation (IAssociation association) {
        this.getAssociations().add(association);
    }

    public void addOccurence (IOccurence occurence) {
        this.getOccurences().add(occurence);
    }

    public IAssociation createAssociation (String id, String reference, String type, String role, String scope, String label) {
        IAssociation association = new Association(this,id,reference, type,role,scope);
        if (label != null) {
            association.setLabel(label);
        }
        this.addAssociation(association);
        return association;
    }

    public IAssociation createAssociation (String reference, String type, String role, String scope, String label) {
        return this.createAssociation(UUID.randomUUID().toString(),reference,type,role,scope,label);
    }

    public IAssociation createAssociation (String reference, String type, String role, String scope) {
        return this.createAssociation(reference,type,role,scope,null);
    }

    public IAssociation createAssociation (String reference, String type, String role) {
        return this.createAssociation(reference,type,role, this.getCurrentScope());
    }

    public IOccurence createOccurence (String reference, String behaviour, Language language, String scope, String role) {
        IOccurence occurence = new Occurence(this,reference,behaviour,language,scope,role);
        this.addOccurence(occurence);
        return occurence;
    }
    
    public IOccurence createOccurence (String reference, String behaviour, String language, String scope, String role) {
    	return this.createOccurence(reference, behaviour, Language.parseLanguageCode(language), scope, role);
    }

    public IOccurence createOccurence (String reference, String behaviour, Language language) {
        return this.createOccurence(reference,behaviour,language,this.getCurrentScope(),"default");
    }

    public IOccurence createOccurence (String reference, String behaviour, String scope) {
        return this.createOccurence(reference,behaviour,Language.Any,scope,"default");
    }

    public IOccurence createOccurence (String reference, String behaviour) {
        return this.createOccurence(reference,behaviour,this.getCurrentLanguage(),this.getCurrentScope(),"default");
    }

    public String generateETag (Language language, String scope) {
        String guid = UUID.randomUUID().toString();
        this.getMeta().setMetaData("etag",language,scope,guid);
        return guid;
    }

    public void toXml (XmlWriter xml) throws IOException {
        xml.writeEntity("topic");
        xml.writeAttribute("id", this.getId());
        xml.writeAttribute("label", this.getLabel());
        this.getMeta().toXml(xml);
        this.getOccurences().toXml(xml);
        this.getAssociations().toXml(xml);
        xml.endEntity();
    }
}
