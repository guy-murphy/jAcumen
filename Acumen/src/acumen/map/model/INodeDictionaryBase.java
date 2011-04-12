package acumen.map.model;

import acumen.util.IAcumenDictionary;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 11:51:57
 */
public interface INodeDictionaryBase<T extends INode> extends IAcumenDictionary<String, T> {
    public INode getParent ();
    public void setParent (T parent);
    public Language getCurrentLanguage ();
    public void setCurrentLanguage (Language language);
    public String getCurrentScope();
    public void setCurrentScope (String scope);
    public boolean isRoot ();
    public void add (T node);
    public void remove (T node);
}
