package acumen.map.model;

import java.util.List;

import acumen.util.IDataType;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 14:04:26
 */
public interface IMetaDataList extends List<IMetaData>, IDataType, INodeChild {
    INode getParent ();
    void setParent (INode parent);
    boolean isRoot ();
    boolean add (IMetaData item);
    String get (String name, Language language, String scope);
    void set (String name, Language language, String scope, String value);
    String get (String name, Language language);
    void set (String name, Language language, String value);
    String get (String name, String scope, String value);
    void set (String name, String scope, String value);
    String get (String name);
    void set (String name, String value);
    IMetaData getMetaData (String name, Language language, String scope);
    IMetaData getMetaData (String name);
    IMetaData getMetaData (String name, Language language);
    IMetaData getMetaData (String name, String scope);
    IMetaData setMetaData (String name, Language language, String scope, String value);
    IMetaData setMetaData (String name, String value);
    IMetaData setMetaData (String name, Language language, String value);
    IMetaData setMetaData (String name, String scope, String value);
    void importMetaData (IMetaDataList list);
    void filterByLanguage ();
    void filterByLanguage (Language language);
    void filterByScope ();
    void filterByScope (String scope);
    void filterByLanguageAndScope ();
    void filterByLanguageAndScope (Language language, String scope);
}
