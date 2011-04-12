package acumen.util;

import acumen.data.xml.XmlWriter;
import java.io.Writer;
import java.io.IOException;

/**
 * User: gmurphy
 * Date: 27-Sep-2009
 * Time: 12:10:36
 */
public interface IDataType {	
    public boolean getPrettyPrint ();
    public void setPrettyPrint (boolean bool);
    public XmlWriter getXmlWriter (Writer writer);
    public String toXml() throws IOException;
    public void toXml (Writer writer) throws IOException;
    public void toXml (XmlWriter writer) throws IOException;
}
