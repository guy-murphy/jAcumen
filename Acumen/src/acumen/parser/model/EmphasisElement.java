package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class EmphasisElement extends WikiElement {
	
	private static final long serialVersionUID = 1842861235127458664L;

	public EmphasisElement () {
		super(null);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(em -");
		this.childrenToString(str, 0);
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("emph");
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
