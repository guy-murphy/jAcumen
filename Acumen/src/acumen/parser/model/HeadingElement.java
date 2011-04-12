package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;


import acumen.data.xml.XmlWriter;

public class HeadingElement extends WikiElement {

	private static final long serialVersionUID = -2747275378091750899L;
	private int _size;

	public HeadingElement(int size) {
		super(null);
		_size = size;
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append(String.format("(heading @size %s", Integer.toString(_size)));
		this.childrenToString(str, 0);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("h"+ Integer.toString(_size));
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
