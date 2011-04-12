package acumen.util;


import java.util.*;
import java.io.IOException;
import java.io.Writer;

import acumen.data.xml.XmlWriter;
import acumen.data.xml.SimpleXmlWriter;
import acumen.data.xml.PrettyPrinterXmlWriter;

/**
 * User: gmurphy
 * Date: 28-Sep-2009
 * Time: 11:59:39
 */
public class AcumenList<T> extends ArrayList<T> implements IAcumenList<T> {

	private static final long serialVersionUID = 7780043382954431737L;
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

    public AcumenList () {
        super();
    }

    public AcumenList (Collection<T> collection) {
        for (T i: collection) {
            this.add(i);
        }
    }
    
    public void push (T item) {
    	this.add(item);
    }
    
    public T pop () {
    	T last = this.peek();
    	this.remove(this.lastIndex());
    	return last;
    }
    
    public T peek (int offset) {
    	return this.get(this.lastIndex() - offset); 
    }
    
    public T peek () {
    	return this.peek(0);
    }
    
    
    public int lastIndex () {
    	return this.size() - 1;
    }
    
    public IAcumenList<T> unionMerge (Collection<T> collection) {
        for (T i: collection) {
            if (!this.contains(i)) {
                this.add(i);
            }
        }
        return this;
    }

    public String toXml () throws IOException {
        return DataType.toXml(this);
    }

    public void toXml (Writer writer) throws IOException {
        DataType.toXml(writer, this);
    }

    public void contentToXml (XmlWriter xml) throws IOException {
        for (T item: this) {
            if (item instanceof IDataType) {
                ((IDataType)item).toXml(xml);
            } else {
                xml.writeEntityWithText("item", item);                
            }
        }
    }

    public void toXml (XmlWriter xml, String label) throws IOException {
        xml.writeEntity(label);
        this.contentToXml(xml);
        xml.endEntity();
    }

    public void toXml (XmlWriter xml) throws IOException {
        this.toXml(xml, "list");
    }

}
