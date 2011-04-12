package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class StrongElement extends WikiElement {
	
	private static final long serialVersionUID = 7638138895761643059L;

	public StrongElement () {
		super(null);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(b -");
		this.childrenToString(str, 0);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("strong");
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
