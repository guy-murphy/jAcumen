package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class TextElement extends WikiElement {

	private static final long serialVersionUID = -5883694584932954493L;

	public TextElement(String content) {
		super(content);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(");
		str.append(this.getOriginal());
		str.append(")");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeText(this.getOriginal());
	}

}
