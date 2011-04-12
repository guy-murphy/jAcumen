package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;
import acumen.parser.scanner.ParseException;

public class TableRowElement extends LineElement {
	
	private static final long serialVersionUID = 6582737278838655237L;

	public TableRowElement () {
		super("row");
	}
	
	public TableRowElement(LineElement element) throws ParseException {
		super(element);
		this.setType("row");
		this.parse();
	}

	public void parseLine (String line) {
		this.getChildren().clear();
		String[] parts = line.split(":");
		for (String part: parts) {
			this.getChildren().add(new TableCellElement(part));
		}
	}
	
	public void parse() throws ParseException {
		StringWriter str = new StringWriter();
		try {
			try {
				for (WikiElement element: this.getChildren()) {
					str.append(element.getOriginal());
				}
				this.parseLine(str.toString());
			} finally {
				str.close();
			}
		} catch (IOException err) {
			throw new ParseException(err.getMessage());
		}
	}
	
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append("(");
		str.append(this.getType());
		str.append("\n");
		this.childrenToString(str, indent+2);
		indent(str, indent);
		str.append(")\n");
	}

} 
