package acumen.map.model;

import acumen.data.xml.XmlWriter;
import acumen.data.xml.SimpleXmlWriter;
import acumen.data.xml.PrettyPrinterXmlWriter;

import java.io.Writer;

/**
 * User: gmurphy
 * Date: 01-Oct-2009
 * Time: 12:21:59
 */
public class TopicDictionary extends NodeDictionaryBase<ITopic> implements ITopicDictionary {

	private static final long serialVersionUID = 6013570968249570371L;
	
	private boolean _prettyPrint;

    public boolean getPrettyPrint () {
        return _prettyPrint;
    }

    public void setPrettyPrint (boolean bool) {
        _prettyPrint = bool;
    }

    public XmlWriter getXmlWriter (Writer writer) {
        XmlWriter simple = new SimpleXmlWriter(writer);
        if (_prettyPrint) {
            return new PrettyPrinterXmlWriter(simple);
        } else {
            return simple;
        }
    }

    public TopicDictionary (ITopicDictionary topics) {
        super(topics);
    }

    public TopicDictionary (INode parent) {
        super(parent);
    }

    public void filterByLanguageAndScope (Language language, String scope) {
        for (ITopic topic: this.values()) {
            topic.setCurrentLanguage(language);
            topic.setCurrentScope(scope);
            topic.getOccurences().filterByLanguageAndScope(language,scope);
            topic.getOccurences().filterByLanguageAndScope(language,scope);
            topic.getAssociations().filterByScope(scope);
        }
    }

    public void filterByLanguage () {
        this.filterByLanguage(this.getCurrentLanguage());
    }

    public void filterByLanguage (Language language) {
        for (ITopic topic: this.values()) {
            topic.setCurrentLanguage(language);
            topic.getMeta().filterByLanguage(language);
            topic.getOccurences().filterByLanguage(language);
        }
    }

    public void filterByScope () {
        this.filterByScope(this.getCurrentScope());
    }

    public void filterByScope (String scope) {
        for (ITopic topic: this.values()) {
            topic.setCurrentScope(scope);
            topic.getMeta().filterByScope(scope);
            topic.getOccurences().filterByScope(scope);
            topic.getAssociations().filterByScope(scope);
        }
    }

}
