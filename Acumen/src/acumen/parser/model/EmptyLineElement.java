package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class EmptyLineElement extends WikiElement {
	
	private static final long serialVersionUID = -5686071860202470119L;

	public EmptyLineElement() {
		super(null);
	}

	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(empty)\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("empty-line");
		xml.endEntity();
	}

}
