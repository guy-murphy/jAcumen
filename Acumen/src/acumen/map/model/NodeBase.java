package acumen.map.model;

import acumen.data.xml.XmlWriter;

import java.util.UUID;
import java.io.IOException;

import acumen.util.DataType;
import acumen.util.Is;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:13:19
 */
public abstract class NodeBase extends DataType implements INode {

    public static final String DEFAULT_SCOPE = "*";
    public static final Language DEFAULT_LANGUAGE = Language.Any;
    
    @Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof INode && obj != null) {
    		INode other = (INode)obj;
    		return this.getId() == null ? other.getId() == null : this.getId().equals(other.getId());
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = 1;
    	hash = hash * 31 + "Node::".hashCode();
    	hash = hash * 31 + (this.getId() == null ? 0 : this.getId().hashCode());
    	return hash;
    }

    private String _id;
    private IMetaDataList _meta;
    private Language _currentLanguage;
    private String _currentScope;
    private INode _parent;

    protected NodeBase () {
        this(null, UUID.randomUUID().toString());
    }

    protected NodeBase (String id) {
        this(null, id);
    }

    protected NodeBase (INode node) {
        this(node.getParent(), node.getId());
        _meta = new MetaDataList(node.getParent());
    }

    protected NodeBase (INode parent, String id) {
        this.setParent(parent);
        this.setId(id);
    }

    public boolean isRoot () {
        return (_parent == null);
    }

    public INode getParent () {
        return _parent;
    }

    public void setParent (INode parent) {
        _parent = parent;
    }

    public String getId () {
        return _id;
    }

    protected void setId (String id) {
        _id = id;
    }

    public IMetaDataList getMeta () {
        if (_meta == null) {
            _meta = new MetaDataList(this);
        }
        return _meta;
    }

    public void setMeta (IMetaDataList meta) {
        _meta = meta;
    }
    
    public IMetaData setMetaData (String name, String language, String scope, String value) {
    	IMetaData meta = this.getMetaData(name, language, scope);
    	if (meta == null) {
    		meta = new MetaData(this.getId(), name, language, scope, value);
    		_meta.add(meta);
    	} else {
    		meta.setValue(value);
    	}
    	return meta;
    }
    
    public IMetaData getMetaData (String name, String language, String scope) {
    	Language lang = Language.parseLanguageCode(language);
    	IMetaDataList meta = this.getMeta();
    	return meta.getMetaData(name, lang, scope);
    }

    public Language getCurrentLanguage () {
        if (this.isRoot()) {
            return _currentLanguage == null ? DEFAULT_LANGUAGE : _currentLanguage;
        } else {
            return this.getParent().getCurrentLanguage();
        }
    }

    public void setCurrentLanguage (Language language) {
        if (this.isRoot()) {
            _currentLanguage = language;
        } else {
            this.getParent().setCurrentLanguage(language);
        }
    }
    
    public void setCurrentLanguage (String language) {
    	this.setCurrentLanguage(Language.parseLanguageCode(language));
    }

    public String getCurrentScope () {
        if (this.isRoot()) {
            return Is.NullOrEmpty(_currentScope) ? DEFAULT_SCOPE : _currentScope;
        } else {
            return this.getParent().getCurrentScope();
        }
    }

    public void setCurrentScope (String scope) {
        if (this.isRoot()) {
            _currentScope = scope;
        } else {
            this.getParent().setCurrentScope(scope);
        }
    }

    public String getLabel () {
        IMetaData meta = this.getMeta().getMetaData("label");
        return (meta == null || Is.NullOrEmpty(meta.getValue())) ? this.getId() : meta.getValue();
    }

    public void setLabel (String label) {
        this.getMeta().setMetaData("label", label);
    }

    public abstract void toXml (XmlWriter xml) throws IOException;

}
