package acumen.map.model;

import acumen.data.xml.XmlWriter;

import java.util.List;
import java.io.IOException;

/**
 * User: gmurphy
 * Date: 29-Sep-2009
 * Time: 11:45:13
 */
public interface IOccurenceList extends List<IOccurence>, INodeChild {
    boolean isRoot ();

    ITopic getParent ();

    void setParent (ITopic parent);

    OccurenceList query (String behaviour, Language language, String scope, String role);

    OccurenceList query (String behaviour, Language language, String scope);

    OccurenceList query (String behaviour, String scope, String role);

    OccurenceList query (String behaviour, String scope);

    void filterByLanguage ();

    void filterByLanguage (Language language);

    void filterByScope ();

    void filterByScope (String scope);

    void filterByLanguageAndScope ();

    void filterByLanguageAndScope (Language language, String scope);

    IOccurence getOccurence (String behaviour, Language language, String scope, String role);

    IOccurence getOccurence (String behaviour, Language language, String role);

    IOccurence getOccurence (String behaviour, String scope, String role);

    IOccurence getOccurence (String behaviour, String scope);

    IOccurence getOccurence (String behaviour, Language language);

    IOccurence getOccurence (String behaviour);

    IOccurence setOccurence (String reference, String behaviour, Language language, String scope, String role);

    IOccurence setOccurence (String reference, String behaviour);

    IOccurence setOccurence (String reference, String behaviour, Language language);

    IOccurence setOccurence (String reference, String behaviour, String scope);

    void importOccurences (IOccurenceList occurences);

    boolean contains (IOccurence occurence);

    boolean add (IOccurence occurence);

    public void toXml (XmlWriter xml) throws IOException;
}
