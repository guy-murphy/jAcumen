package acumen.util;

import java.util.Map;
import java.io.IOException;

import acumen.data.xml.XmlWriter;

/**
 * User: gmurphy
 * Date: 30-Sep-2009
 * Time: 12:02:28
 */
public interface IAcumenDictionary<TKey,TValue> extends Map<TKey,TValue>, IDataType {
    public void toXml (XmlWriter xml, String label) throws IOException;
}
