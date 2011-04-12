package acumen.map.model;

import acumen.data.xml.XmlWriter;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 12:17:39
 */
public class MetaData extends ScopedDataBase implements IMetaData {

    private String _parentId;
    private String _name;
    private String _value;

    public MetaData (String parentId, String name, String scope, String value) {
        this(parentId, name, Language.Any, scope, value);
    }

    public MetaData (String parentId, String name, Language language, String value) {
        this(parentId, name, language, NodeBase.DEFAULT_SCOPE, value);
    }

    public MetaData (String parentId, String name, String value) {
        this(parentId, name, Language.Any, NodeBase.DEFAULT_SCOPE, value);
    }
    
    public MetaData (String parentId, String name, String language, String scope, String value) {
    	this(parentId, name, Language.parseLanguageCode(language), scope, value);
    }

    public MetaData (String parentId, String name, Language language, String scope, String value) {
        super(language, scope);
        _parentId = parentId;
        _name = name;
        _value = value;
    }

    public MetaData (IMetaData meta) {
        this(meta.getParentId(), meta.getName(), meta.getLanguage(), meta.getScope(), meta.getValue());
    }
    
    @Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof IMetaData && obj != null) {
    		IMetaData other = (IMetaData)obj;
    		return (
    			super.equals((ScopedDataBase)other) && // ensure base class equals match
    			this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()) && 
    			this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()) &&
    			this.getValue() == null ? other.getValue() == null : this.getValue().equals(other.getValue())
    		);
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = super.hashCode();
    	hash = hash * 31 + "MetaData::".hashCode();
    	hash = hash * 31 + this.getParentId() == null ? 0 : this.getParentId().hashCode();
    	hash = hash * 31 + this.getName() == null ? 0 : this.getName().hashCode();
    	hash = hash * 31 + this.getValue() == null ? 0 : this.getValue().hashCode();
    	return hash;
    }

    public String getParentId () {
        return _parentId;
    }

    public void setParentId (String parentId) {
        _parentId = parentId;
    }

    public String getName () {
        return _name;
    }

    public void setName (String name) {
        _name = name;
    }

    public String getValue () {
        return _value;
    }

    public void setValue (String value) {
        _value = value;
    }

    public void toXml (XmlWriter xml) throws IOException {
        xml.writeEntity("item");
        xml.writeAttribute("name", this.getName());
        xml.writeAttribute("value", this.getValue());
        super.toXml(xml);
        xml.endEntity();
    }

}
