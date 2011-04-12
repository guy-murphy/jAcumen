package acumen.map.model;

import acumen.util.AcumenDictionary;
import acumen.util.IDataType;
import acumen.util.ToXml;
import acumen.util.Is;
import acumen.data.xml.XmlWriter;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 15:14:02
 */
public class Occurence extends ScopedDataBase implements IOccurence {

    public static boolean equals (Occurence o1, Occurence o2) {
        return (o1 == o2) || !((o1 == null) || (o2 == null)) && o1.equals(o2);
    }

    private String _reference;
    private String _behaviour;
    private INode _parent;
    private AcumenDictionary<String,IDataType> _resolvedObjects;
    private String _content;
    private String _role;

    public Occurence (IOccurence occurence) {
        super(occurence);

        _resolvedObjects = null;
        _parent = occurence.getParent();
        _reference = occurence.getReference();
        _behaviour = occurence.getBehaviour();
        _role = occurence.getRole();
    }

    public Occurence(INode parent, String reference, String behaviour, Language language, String scope, String role) {
        super(language, scope);

        _parent = parent;
        _reference = reference;
        _behaviour = behaviour;
        _role = role;
    }
    
    public Occurence(INode parent, String reference, String behaviour, String language, String scope, String role) {
    	this(parent, reference, behaviour, Language.parseLanguageCode(language), scope, role);
    }

    public Occurence (INode parent, String reference, String behaviour, String role) {
        super();

        _parent = parent;
        _reference = reference;
        _behaviour = behaviour;
        _role = role;
    }
    
    @Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof IOccurence && obj != null) {
    		IOccurence other = (IOccurence)obj;
    		return (
    			super.equals((ScopedDataBase)other) && // ensure base class equals match
    			this.getReference() == null ? other.getReference() == null : this.getReference().equals(other.getReference()) && 
    			this.getBehaviour() == null ? other.getBehaviour() == null : this.getBehaviour().equals(other.getBehaviour()) &&
    			this.getRole() == null ? other.getRole() == null : this.getRole().equals(other.getRole())
    		);
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = super.hashCode();
    	hash = hash * 31 + "Occurence::".hashCode();
    	hash = hash * 31 + this.getReference() == null ? 0 : this.getReference().hashCode();
    	hash = hash * 31 + this.getBehaviour() == null ? 0 : this.getBehaviour().hashCode();
    	hash = hash * 31 + this.getRole() == null ? 0 : this.getRole().hashCode();
    	return hash;
    }

    public boolean isRoot () {
        return _parent == null;
    }

    public INode getParent () {
        return _parent;
    }

    public void setParent (INode parent) {
        _parent = parent;
    }

    public String getReference () {
        return _reference;
    }

    public void setReference (String reference) {
        _reference = reference;
    }

    public String getBehaviour () {
        return _behaviour;
    }

    public void setBehaviour (String behaviour) {
        _behaviour = behaviour;
    }

    public AcumenDictionary<String,IDataType> getResolvedObjects () {
        if (_resolvedObjects == null) _resolvedObjects = new AcumenDictionary<String,IDataType>();
        return _resolvedObjects;
    }
    
    public void setResolvedObjects (AcumenDictionary<String,IDataType> resolvedObjects) {
    	_resolvedObjects = resolvedObjects;
    }

    public String getContent () {
        return _content;
    }

    public void setContent (String content) {
        _content = content;
    }

    public String getRole () {
        return _role;
    }

    public void setRole (String role) {
        _role = role;
    }

    public boolean equals (Occurence other) {
	    if (this == other) return true;
        if (other == null) return false;
        return true;

	}

    public void toXml (XmlWriter xml) throws IOException {
        xml.writeEntity("occurence");
        xml.writeAttribute("reference", this.getReference());
        xml.writeAttribute("behaviour", this.getBehaviour());
        xml.writeAttribute("language", this.getLanguage());
        xml.writeAttribute("scope", this.getScope());
        xml.writeAttribute("role", this.getRole());
        if (_resolvedObjects != null) {
            ToXml.Dictionary("items", _resolvedObjects, xml);    
        }
        if (!Is.NullOrEmpty(_content)) {
            xml.writeRawText(_content);
        }
        xml.endEntity();
    }
}
