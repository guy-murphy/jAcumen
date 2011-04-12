package acumen.map.model;

import acumen.data.xml.XmlWriter;

import java.io.IOException;

import acumen.util.DataType;
import acumen.util.Is;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 12:05:57
 */
public abstract class ScopedDataBase extends DataType implements IScopedData {

    private Language _language;
    private String _scope;
    
    @Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof IScopedData && obj != null) {
    		IScopedData other = (IScopedData)obj;
    		return (
    			this.getScope() == null ? other.getScope() == null : this.getScope().equals(other.getScope()) &&
    			this.getLanguage().equals(other.getLanguage())
    		);
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = 1;
    	hash = hash * 31 + "Scoped::".hashCode();
    	hash = hash * 31 + this.getScope() == null ? 0 : this.getScope().hashCode();
    	hash = hash * 31 + this.getLanguage().hashCode();
    	return hash;
    }

    protected ScopedDataBase () {
        this(NodeBase.DEFAULT_LANGUAGE, NodeBase.DEFAULT_SCOPE);
    }

    protected ScopedDataBase (IScopedData data) {
        this(data.getLanguage(), data.getScope());    
    }

    protected ScopedDataBase (String scope) {
        this(NodeBase.DEFAULT_LANGUAGE, scope);
    }

    protected ScopedDataBase (Language language) {
        this(language, NodeBase.DEFAULT_SCOPE);
    }

    protected ScopedDataBase (Language language, String scope) {
        _language = language;
        _scope = scope;
    }

    public Language getLanguage () {
        return (_language == null) ? NodeBase.DEFAULT_LANGUAGE : _language;
    }

    public void setLanguage (Language language) {
        _language = language;
    }

    public String getScope () {
        return Is.NullOrEmpty(_scope) ? NodeBase.DEFAULT_SCOPE : _scope;
    }

    public void setScope (String scope) {
        _scope = scope;
    }

    public void toXml (XmlWriter xml) throws IOException {
        xml.writeAttribute("language", this.getLanguage());
        xml.writeAttribute("scope", this.getScope());
    }

}
