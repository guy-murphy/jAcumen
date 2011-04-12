/**
 * User: Guy J. Murphy
 * Date: Apr 7, 2010
 * Time: 4:12:45 PM
 * File: InlineCodeElement.java
 */
package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

/**
 * @author Guy J. Murphy
 */
public class InlineCodeElement extends WikiElement {
	
	private static final long serialVersionUID = 7193994914027614788L;

	public InlineCodeElement () {
		super(null);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(code -");
		this.childrenToString(str, 0);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("code");
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
