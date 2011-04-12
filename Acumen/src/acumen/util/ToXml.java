package acumen.util;

import acumen.data.xml.XmlWriter;

import java.util.Map;
import java.io.IOException;

/**
 * User: gmurphy Date: 06-Oct-2009 Time: 13:48:18
 */
public class ToXml {
	public static void Dictionary(String label, Map<String, String> dictionary, XmlWriter writer) throws IOException {
		writer.writeEntity(label);
		for (Map.Entry<String, String> item : dictionary.entrySet()) {
			DictionaryStringItemToXml(item.getKey(), item.getValue(), writer);
		}
		writer.endEntity();// end "name"
	}

	public static void List(String label, IAcumenList<IDataType> list, XmlWriter writer) throws IOException {
		writer.writeEntity(label);
		for (IDataType item : list) {
			item.toXml(writer);
		}
		writer.endEntity();
	}

	public static void Dictionary(String label, IAcumenDictionary<String, IDataType> dictionary, XmlWriter writer) throws IOException {
		writer.writeEntity(label);
		for (Map.Entry<String, IDataType> item : dictionary.entrySet()) {
			item.getValue().toXml(writer);
		}
		writer.endEntity();// end "name"
	}

	public static void DictionaryStringItemToXml(String key, String value, XmlWriter writer) throws IOException {
		writer.writeEntity("item");
		writer.writeAttribute("name", key);
		writer.writeAttribute("value", value);
		writer.endEntity();// end item
	}
}
