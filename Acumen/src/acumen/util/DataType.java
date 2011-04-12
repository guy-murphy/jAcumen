package acumen.util;


import acumen.data.xml.XmlWriter;
import acumen.data.xml.SimpleXmlWriter;
import acumen.data.xml.PrettyPrinterXmlWriter;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:11:44
 */
public abstract class DataType implements IDataType {

    private boolean _prettyPrint;
    
    public abstract boolean equals(Object obj);
    public abstract int hashCode();

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

    public static String toXml (IDataType data) throws IOException {
        StringWriter str = new StringWriter();
        try {
            data.toXml(str);
            return str.toString();
        } finally {
            str.close();
        }
    }

    public static void toXml (Writer writer, IDataType data) throws IOException {
        XmlWriter xml = data.getXmlWriter(writer);
        try {
            data.toXml(xml);
        } finally {
            xml.close();
        }
    }

    public String toXml () throws IOException {
        return DataType.toXml(this);
    }

    public void toXml (Writer writer) throws IOException {
        XmlWriter xml = this.getXmlWriter(writer);
        try {
            this.toXml(xml);
        } finally {
            xml.close();
        }
    }

    public abstract void toXml (XmlWriter xml) throws IOException;
}
