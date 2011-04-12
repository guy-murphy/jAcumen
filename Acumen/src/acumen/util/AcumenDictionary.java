package acumen.util;


import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.Writer;

import acumen.data.xml.XmlWriter;
import acumen.data.xml.SimpleXmlWriter;
import acumen.data.xml.PrettyPrinterXmlWriter;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 15:27:11
 */
public class AcumenDictionary<TKey,TValue> extends HashMap<TKey,TValue> implements IAcumenDictionary<TKey,TValue> {

	private static final long serialVersionUID = -4143763779974547420L;
	private boolean _prettyPrint;
    private String _label;

    public String getLabel () {
        if (Is.NullOrEmpty(_label)) {
            _label = "dictionary";
        }
        return _label;
    }

    public void setLabel (String label) {
        _label = label;
    }

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

    public AcumenDictionary () {
        super();
    }

    public AcumenDictionary (String label) {
        super();
        this.setLabel(label);
    }

    public AcumenDictionary (IAcumenDictionary<TKey,TValue> dictionary) {
        super(dictionary);
    }
    
    public String toXml () throws IOException {
        return DataType.toXml(this);
    }

    public void toXml (Writer writer) throws IOException {
        DataType.toXml(writer, this);
    }

    public void toXml (XmlWriter xml) throws IOException {
        this.toXml(xml, this.getLabel());
    }

    public void toXml (XmlWriter xml, String label) throws IOException {
        xml.writeEntity(label);
        for (Map.Entry<TKey,TValue> entry: this.entrySet()) {
            if (entry.getValue() instanceof IDataType) {
                xml.writeEntity("item");
                xml.writeAttribute("name", entry.getKey());
                ((IDataType)entry.getValue()).toXml(xml);
                xml.endEntity();
            } else {
                xml.writeEntity("item");
                xml.writeAttribute("name", entry.getKey());
                if (entry.getValue() != null) {
                    xml.writeAttribute("value", entry.getValue());
                }
                xml.endEntity();
            }
        }
        xml.endEntity();
    }

}
