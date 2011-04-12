package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class DocumentElement extends WikiElement {

	private static final long serialVersionUID = 5281617899468421197L;

	public DocumentElement() {
		super(null);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(wiki\n");
		this.childrenToString(str, indent);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("wiki");
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
