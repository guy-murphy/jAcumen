package acumen.map.model;

import acumen.util.IDataType;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:13:58
 */
public interface INode extends INodeChild, IDataType {
    public String getId ();
    public void setParent(INode parent);
    public Language getCurrentLanguage ();
    public void setCurrentLanguage (Language language);
    public void setCurrentLanguage (String language);
    public String getCurrentScope ();
    public void setCurrentScope (String scope);
    public IMetaDataList getMeta ();
    void setMeta (IMetaDataList meta);
    public String getLabel ();
    public void setLabel (String label);
    
    IMetaData setMetaData (String name, String language, String scope, String value);
    IMetaData getMetaData (String name, String language, String scope);
}
