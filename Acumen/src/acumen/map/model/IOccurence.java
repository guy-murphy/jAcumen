package acumen.map.model;

import acumen.util.AcumenDictionary;
import acumen.util.IDataType;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 16:05:50
 */
public interface IOccurence extends IScopedData, INodeChild {
    boolean isRoot ();

    INode getParent ();

    void setParent (INode parent);

    String getReference ();

    void setReference (String reference);

    String getBehaviour ();

    void setBehaviour (String behaviour);

    AcumenDictionary<String,IDataType> getResolvedObjects ();
    
    void setResolvedObjects (AcumenDictionary<String,IDataType> resolvedObjects);

    String getContent ();

    void setContent (String content);

    String getRole ();

    void setRole (String role);

    boolean equals (Occurence other);

    int hashCode ();
}
