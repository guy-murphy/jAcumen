package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.parser.scanner.ParseException;

import acumen.data.xml.XmlWriter;

public class BlockElement extends WikiElement {

	private static final long serialVersionUID = -2337439188065807222L;
	
	private String _type; 

	public BlockElement() {
		super(null);
		_type = "block";
	}
	
	public BlockElement(String type) {
		super(null);
		_type = type;
	}
	
	public void finalise () throws ParseException {
		// we might have multiple paragraphs
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(");
		str.append(_type);
		str.append("\n");
		this.childrenToString(str, indent+2);
		indent(str, indent);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity(_type);
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
