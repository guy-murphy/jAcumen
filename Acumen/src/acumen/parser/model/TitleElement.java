package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class TitleElement extends TextElement {
	
	private static final long serialVersionUID = -818121024011332166L;

	public TitleElement (String text) {
		super(text);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(title ");
		str.append(this.getOriginal().trim());
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("title");
		xml.writeText(this.getOriginal().trim());
		xml.endEntity();
	}

}
