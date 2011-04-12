/**
 * User: Guy J. Murphy
 * Date: Feb 16, 2010
 * Time: 4:31:47 PM
 * File: PropertySet.java
 */
package acumen.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @author Guy J. Murphy
 */
public class PropertySet extends AcumenDictionary<String,String> {

	private static final long serialVersionUID = -2218988356262591099L;
	
	public void fromXml(Document data) {
		this.clear();
		NodeList items = data.getElementsByTagName("item");
		for (int i = 0; i < items.getLength(); i++) {
			Element item = (Element) items.item(i);
			String name = item.getAttribute("name");
			String value = item.getAttribute("value");
			if (Is.NotNullOrEmpty(name) && Is.NotNullOrEmpty(value)) {
				this.put(name, value);
			}
		}
	}

}
