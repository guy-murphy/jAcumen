package acumen.map.model;

import acumen.util.AcumenList;

import acumen.data.xml.XmlWriter;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 16:25:15
 */

public class OccurenceList extends AcumenList<IOccurence> implements IOccurenceList {

	private static final long serialVersionUID = -8066999007128782849L;
	
	private ITopic _parent;

    public OccurenceList (ITopic parent) {
        _parent = parent;
    }

    public OccurenceList (IOccurenceList list) {
        super(list);
        _parent = list.getParent();
    }

    public boolean isRoot () {
        return _parent == null;
    }

    public ITopic getParent () {
        return _parent;
    }

    public void setParent (ITopic parent) {
        _parent = parent;
    }

    public OccurenceList query (String behaviour, Language language, String scope, String role) {
        Language languageToUse = (language == Language.None) ? _parent.getCurrentLanguage() : language;
        String scopeToUse = (scope != null) ? scope : _parent.getCurrentScope();

        OccurenceList result = new OccurenceList(_parent);
        for (IOccurence item: this) {
            if (
                (behaviour == null||item.getBehaviour().equals(behaviour)) &&
                (item.getScope().equals(scopeToUse)) &&
                (role == null || item.getRole().equals(role)) &&
                (item.getLanguage() == Language.None || item.getLanguage() == languageToUse)
            ) {
                result.add(item);
            }
        }
        return result;        
    }

    public OccurenceList query (String behaviour, Language language, String scope) {
        return this.query(behaviour, language, scope, null);
    }

    public OccurenceList query (String behaviour, String scope, String role) {
        return this.query(behaviour, Language.None, scope, role);
    }

    public OccurenceList query (String behaviour, String scope) {
        return this.query(behaviour, Language.None, scope, null);
    }

    public void filterByLanguage () {
        this.filterByLanguage(Language.None);
    }

    public void filterByLanguage (Language language) {
        this.filterByLanguageAndScope(language, null);
    }

    public void filterByScope () {
        this.filterByScope(null);
    }

    public void filterByScope (String scope) {
        this.filterByLanguageAndScope(Language.None, scope);
    }

    public void filterByLanguageAndScope () {
        this.filterByLanguageAndScope(Language.None, null);
    }

    public void filterByLanguageAndScope (Language language, String scope) {
        Language languageToUse = (language == Language.None) ? _parent.getCurrentLanguage() : language;
        String scopeToUse = (scope == null || scope.length() == 0) ? _parent.getCurrentScope() : scope;
        
        OccurenceList languageAny = this.query(null, Language.Any, scope);
        if (languageToUse == Language.None || languageToUse == Language.Any) {
            this.clear();
            for (IOccurence item: languageAny) {
                this.add(item);
            }
        } else {
            OccurenceList languageSpecific = this.query(null, languageToUse, scopeToUse);
            this.clear();
            for (IOccurence item: languageAny) {
                OccurenceList specificOccurences = languageSpecific.query(item.getBehaviour(), languageToUse, scopeToUse, item.getRole());
                if (specificOccurences.size() == 0) {
                    this.add(item);
                }
            }
            for (IOccurence item: languageSpecific) {
                this.add(item);
            }
        }
    }

    public IOccurence getOccurence (String behaviour, Language language, String scope, String role) {
        // use the parent language if none is specified
        Language languageToUse = (language == Language.None) ? _parent.getCurrentLanguage() : language;
        // use the parent scope if none is specified
        String scopeToUse = (scope == null || scope.length() == 0) ? _parent.getCurrentScope() : scope;
        // use the default role if none is specified
        String roleToUse = (role == null || role.length() == 0) ? "default" : role;

        IOccurence anyMatch = null;

        for (IOccurence item: this) {
            if (item.getBehaviour().equals(behaviour)) {
                if (item.getLanguage() == languageToUse || item.getLanguage() == Language.Any) {
                    if (item.getScope().equals(scopeToUse)) {
                        if (item.getRole().equals(roleToUse)) {
                            // we have a possible match
                            // if it's ambiguous, store and find better
                            // if it's a specific return it
                            if (item.getLanguage() == languageToUse) {
                                return item;
                            } else {
                                anyMatch = item;
                            }
                        }
                    }
                }
            }
        }
        // we didn't find a specific match so return the anyMatch
        // on the offchance we hit a none specific language match
        return anyMatch;
    }

    public IOccurence getOccurence (String behaviour, Language language, String role) {
        return this.getOccurence(behaviour, language, null, role);
    }

    public IOccurence getOccurence (String behaviour, String scope, String role) {
        return this.getOccurence(behaviour, Language.None, scope, role);
    }

    public IOccurence getOccurence (String behaviour, String scope) {
        return this.getOccurence(behaviour, Language.None, scope, null);
    }

    public IOccurence getOccurence (String behaviour, Language language) {
        return this.getOccurence(behaviour, language, null, null);
    }

    public IOccurence getOccurence (String behaviour) {
        return this.getOccurence(behaviour, Language.None, null, null);
    }

    public IOccurence setOccurence (String reference, String behaviour, Language language, String scope, String role) {
        // does this occurence already exist?
        IOccurence occurence = null;
        for (IOccurence item: this) {
            if (
                item.getReference().equals(reference) &&
                item.getBehaviour().equals(behaviour) &&
                item.getLanguage() == language &&
                item.getScope().equals(scope) &&
                item.getRole().equals(role)
            ) {
                occurence = item;
                break;
            }
        }
        // if it doesn't exist add it
        if (occurence == null) {
            occurence = new Occurence(_parent, reference, behaviour, language, scope, role);
            this.add(occurence);
        }
        return occurence;
    }

    public IOccurence setOccurence (String reference, String behaviour) {
        return this.setOccurence(reference, behaviour, this.getParent().getCurrentLanguage(), this.getParent().getCurrentScope(), "default");
    }

    public IOccurence setOccurence (String reference, String behaviour, Language language) {
        return this.setOccurence(reference, behaviour, language, this.getParent().getCurrentScope(), "default");
    }

    public IOccurence setOccurence (String reference, String behaviour, String scope) {
        return this.setOccurence(reference, behaviour, this.getParent().getCurrentLanguage(), scope, "default");
    }

    public void importOccurences (IOccurenceList occurences) {
        for (IOccurence item: occurences) {
            this.setOccurence(item.getReference(), item.getBehaviour(), item.getLanguage(), item.getScope(), item.getRole());
        }
    }

    public boolean contains (IOccurence occurence) {
        for (IOccurence item: this) {
            if (
                occurence.getBehaviour().equals(item.getBehaviour()) &&
                occurence.getLanguage() == item.getLanguage() &&
                occurence.getReference().equals(item.getReference()) &&
                occurence.getRole().equals(item.getRole()) &&
                occurence.getScope().equals(item.getScope())
            ) {
                return true;
            }
        }
        return false;
    }

    public boolean add (IOccurence occurence) {
        if (!this.contains(occurence)) {
            occurence.setParent(this.getParent());
            super.add(occurence);
        }
        return true; // yes this is shit, yes it's as per the java reference implementation
    }

    public void toXml (XmlWriter xml) throws IOException {
        super.toXml(xml, "occurences");
    }
}
