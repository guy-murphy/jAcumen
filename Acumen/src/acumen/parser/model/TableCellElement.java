package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

public class TableCellElement extends TextElement {
	
	private static final long serialVersionUID = 8238545453560299024L;

	public TableCellElement (String original) {
		super(original);
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(cell -");
		str.append(this.getOriginal());
		str.append(")\n");
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("td");
		xml.writeText(this.getOriginal());
		xml.endEntity();
	}

}
