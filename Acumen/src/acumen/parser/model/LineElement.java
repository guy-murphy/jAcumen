package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class LineElement extends WikiElement {

	private static final long serialVersionUID = 3798928849333452721L;
	
	private String _type;

	public LineElement() {
		super(null);
		_type = "line";
	}
	
	public LineElement (LineElement element) {
		this();
		for (WikiElement item: element.getChildren()) {
			this.getChildren().add(item);
		}
	}
	
	public LineElement(String type) {
		super(null);
		_type = type;
	}
	
	public void setType (String type) {
		_type = type;
	}
	
	public String getType () {
		return _type;
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(");
		str.append(_type);
		str.append(" -");
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
