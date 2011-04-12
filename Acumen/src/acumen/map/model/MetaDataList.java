package acumen.map.model;

import acumen.util.AcumenList;

import acumen.data.xml.XmlWriter;
import java.util.ArrayList;
import java.io.IOException;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 12:28:59
 */
public class MetaDataList extends AcumenList<IMetaData> implements IMetaDataList {

	private static final long serialVersionUID = 3951293800151231070L;
	private INode _parent;

    public MetaDataList (INode parent) {
        _parent = parent;        
    }

    public MetaDataList (IMetaDataList list) {
        _parent = list.getParent();
        for (IMetaData i: list) {
            this.add(i);
        }
    }

    @Override
    public boolean contains (Object item) {
    	if (item instanceof IMetaData) {
    		IMetaData m = (IMetaData)item;
	        for (IMetaData i: this) {
	            if (m.equals(i)) {
	                return true;
	            }
	        }
    	}
        return false;
    }

    public INode getParent () {
        return _parent;
    }

    public void setParent (INode parent) {
        _parent = parent;
    }

    public boolean isRoot () {
        return (_parent == null);
    }

    public boolean add (IMetaData item) {
        boolean added = false;
        if (!this.contains(item)) {
            added = true;
        }
        this.setMetaData(item.getName(), item.getLanguage(), item.getScope(), item.getValue());
        return added;
    }

    public String get (String name, Language language, String scope) {
        IMetaData item = this.getMetaData(name, language, scope);
        return (item == null) ? null : item.getValue();
    }

    public void set (String name, Language language, String scope, String value) {
        this.setMetaData(name, language, scope, value);
    }

    public String get (String name, Language language) {
        return this.get(name, language, _parent.getCurrentScope());
    }

    public void set (String name, Language language, String value) {
        this.set(name, language, _parent.getCurrentScope(), value);
    }

    public String get (String name, String scope, String value) {
        return this.get(name, _parent.getCurrentLanguage(), scope);
    }

    public void set (String name, String scope, String value) {
        this.set(name, _parent.getCurrentLanguage(), scope, value);
    }

    public String get (String name) {
        return this.get(name, _parent.getCurrentLanguage(), _parent.getCurrentScope());
    }

    public void set (String name, String value) {
        this.set(name, _parent.getCurrentLanguage(), _parent.getCurrentScope(), value);
    }

    protected IMetaData getExactMetaData (String name, Language language, String scope) {
        for (IMetaData i: this) {
            if (i.getName().equals(name) && i.getScope().equals(scope) && i.getLanguage() == language) {
                return i;
            }
        }
        return null;
    }

    public IMetaData getMetaData (String name, Language language, String scope) {
        IMetaData exactMatch = null;
        IMetaData anyMatch = null;

        for (IMetaData i: this) {
            if (i.getName().equals(name) && i.getScope().equals(scope)) {
                if (i.getLanguage() == Language.Any) {
                    anyMatch = i;
                }
                if (i.getLanguage() == language) {
                    exactMatch = i;
                }
            }
        }

        return (exactMatch != null) ? exactMatch : anyMatch;
    }

    public IMetaData getMetaData (String name) {
        IMetaData match = this.getMetaData(name, _parent.getCurrentLanguage(), _parent.getCurrentScope());
        if (match == null) {
            match = this.getMetaData(name, Language.Any, _parent.getCurrentScope());            
        }
        return match;
    }

    public IMetaData getMetaData (String name, Language language) {
        IMetaData match = this.getMetaData(name, language, _parent.getCurrentScope());
        if (match == null) {
            match = this.getMetaData(name, Language.Any, _parent.getCurrentScope());
        }
        return match;
    }

    public IMetaData getMetaData (String name, String scope) {
        IMetaData match = this.getMetaData(name, _parent.getCurrentLanguage(), scope);
        if (match == null) {
            match = this.getMetaData(name, Language.Any, scope);
        }
        return match;
    }

    public IMetaData setMetaData (String name, Language language, String scope, String value) {
        Language languageToUse = (language == Language.None) ? _parent.getCurrentLanguage() : language;
        String scopeToUse = (scope == null || scope.length() == 0) ? _parent.getCurrentScope() : scope;

        IMetaData item = this.getExactMetaData(name, languageToUse, scopeToUse);
        if (item != null) {
            this.remove(item);
        }
        if (value == null || value.length() == 0) {
            return null;
        } else {
            item = new MetaData(_parent.getId(), name, languageToUse, scopeToUse, value);
            super.add(item);
            return item;
        }
    }

    public IMetaData setMetaData (String name, String value) {
        return this.setMetaData(name, _parent.getCurrentLanguage(), _parent.getCurrentScope(), value);
    }

    public IMetaData setMetaData (String name, Language language, String value) {
        return this.setMetaData(name, language, _parent.getCurrentScope(), value);
    }

    public IMetaData setMetaData (String name, String scope, String value) {
        return this.setMetaData(name, _parent.getCurrentLanguage(), scope, value);
    }

    public void importMetaData (IMetaDataList list) {
        for (IMetaData i: list) {
            this.setMetaData(i.getName(), i.getLanguage(), i.getScope(), i.getValue());
        }
    }

    /* AXIOMS FOR LANGUAGE AND SCOPE
		 * #1 Scope - "*" is the universal scope but it is never infered on query failure
		 * #2 Language - Language.Any is the variant Language and is infered on query failure
		 * #3 Role - "default" is the default Role but it is never infered on query failure
    */

    public void filterByLanguage () {
        this.filterByLanguage(Language.None);
    }

    public void filterByLanguage (Language language) {
        // filter first by the specified language
        ArrayList<IMetaData> meta = new ArrayList<IMetaData>();
        for (IMetaData i: this) {
            if (i.getLanguage() == language) {
                meta.add(i);
            }
        }
        // if there's not matches for the specified
        // language try the same thing with Language.Any
        if (meta.size() == 0) {
            for (IMetaData i: this) {
                if (i.getLanguage() == Language.Any) {
                    meta.add(i);
                }
            }
        }
        // doctor *this* list so that it conforms
        this.clear();
        for (IMetaData i: meta) {
            this.add(i);
        }
    }

    public void filterByScope () {
        this.filterByScope(null);
    }

    public void filterByScope (String scope) {
        MetaDataList anyMeta = new MetaDataList(_parent);
        MetaDataList referenceList = new MetaDataList(this);

        String scopeToUse = (scope == null) ? _parent.getCurrentScope() : scope;

        for (IMetaData i: referenceList) {
            if (i.getScope().equals("*")) {
                anyMeta.add(i);
            }
            if (!i.getScope().equals(scopeToUse)) {
                this.remove(i);
            }
        }

        for (IMetaData i: anyMeta) {
            if (this.getMetaData(i.getName(), scopeToUse) == null) {
                this.add(i);
            }
        }
    }

    public void filterByLanguageAndScope () {
        this.filterByLanguageAndScope(Language.None, null);
    }

    public void filterByLanguageAndScope (Language language, String scope) {
        // use the parent language if none is specified
        Language languageToUse = (language == Language.None) ? _parent.getCurrentLanguage() : language;
        // use the parents scope if none is specified
        String scopeToUse = (scope == null) ? _parent.getCurrentScope() : scope;

        MetaDataList possible = new MetaDataList(_parent);
        MetaDataList reference = new MetaDataList(this);

        for (IMetaData i: reference) {
            if (i.getLanguage() == Language.Any && !i.getScope().equals(scopeToUse)) {
                possible.add(i);
            }
            if (!i.getScope().equals(scopeToUse) || i.getLanguage() != languageToUse) {
                this.remove(i);
            }
        }

        for (IMetaData i: possible) {
            if (this.getMetaData(i.getName(), languageToUse, scopeToUse) == null) {
                this.add(i);
            }
        }
    }

    public void toXml (XmlWriter xml) throws IOException {
        super.toXml(xml, "metadata");
    }

}
