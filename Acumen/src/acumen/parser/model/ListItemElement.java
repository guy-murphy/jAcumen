package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class ListItemElement extends LineElement {

	private static final long serialVersionUID = -1207882236163823167L;
	
	private int _level;
	
	public int getLevel () {
		return _level;
	}
	
	public void setLevel (int level) {
		_level = level;
	}
	
	public int incLevel () {
		return _level++;
	}
	
	public int decLevel () {
		return _level--;
	}
	
	public ListItemElement () {
		super("item");
		_level = 0;
	}
		
	@Override
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(");
		str.append(this.getType());
		str.append(" @level ");
		str.append(Integer.toString(this.getLevel()));
		str.append(" -");
		this.childrenToString(str, indent+2);
		indent(str, indent);
		str.append(")\n");
	}
	
	@Override
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity(this.getType());
		xml.writeAttribute("level", Integer.toString(this.getLevel()));
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
