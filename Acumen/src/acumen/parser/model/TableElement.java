package acumen.parser.model;

import acumen.parser.scanner.ParseException;
import acumen.util.AcumenList;

public class TableElement extends BlockElement {
	
	private static final long serialVersionUID = 1985215716321943636L;
	
	public TableElement () {
		super("table");
	}
	
	@Override
	public void finalise () throws ParseException {
		// turn the line element children into table rows
		AcumenList<WikiElement> rows = new AcumenList<WikiElement>();
		for (WikiElement line: this.getChildren()) {
			TableRowElement row = new TableRowElement((LineElement)line);
			rows.add(row);
		}
		this.clearChildren();
		this.setChildren(rows);
	}
	
//	@Override
//	public WikiElement addElement (WikiElement element) throws ParseException {
//		if (element instanceof TableRowElement) {
//			this.getChildren().add(element);
//			return element;
//		} else if (element instanceof LineElement) {
//			// we need to turn this into a TableRowElement
//			// before we add it
//			TableRowElement row = new TableRowElement((LineElement)element);
//			this.addElement(row);
//			return row;
//		} else {
//			throw new ParseException("You may only add LineElement or TableRowElement to a TableElement.");
//		}
//	}

}
