package acumen.parser.model;

import acumen.parser.scanner.ParseException;
import acumen.util.AcumenList;

public class ListElement extends BlockElement {
	
	private static final long serialVersionUID = 3356167044852725196L;
	
	private int _level;

	public ListElement () {
		super("list");
		_level = 0;
	}
	
	public int getLevel () {
		return _level;
	}
	
	public int setLevel(int level) {
		_level = level;
		return _level;
	}
	
	public void finalise () throws ParseException {
		// copy the current list content sideways
		// to process and put back
		AcumenList<WikiElement> content = new AcumenList<WikiElement>(this.getChildren());
		this.clearChildren();
		AcumenList<ListElement> context = new AcumenList<ListElement>();
		context.push(this); // this is the root element
		
		for (WikiElement element: content) {
			ListItemElement item = (ListItemElement)element;
			if (item.getLevel() > context.size()) {
				// we entering a new list
				ListElement sub = new ListElement();
				context.peek().addElement(sub);
				context.push(sub);
			} else if (item.getLevel() < context.size()) {
				// we're leaving a sub-list to its parent
				context.pop();
			}
			context.peek().addElement(element);
		}
	}
	
	/**
	 * Processes the flat list of child items into a
	 * tree of sublists. This is much easier (for me)
	 * to do here that bending the formal parser into
	 * arcane shapes.
	 * 
	 * @throws ParseException
	 */
	public void postProcess () throws ParseException {
		// copy the children sideways
		// and reprocess them into sub lists
		AcumenList<WikiElement> children = new AcumenList<WikiElement>(this.getChildren());
		AcumenList<ListElement> lists = new AcumenList<ListElement>();
		this.clearChildren();
		
		int currentItemLevel, currentListLevel; // helps with debugging
		lists.push(this);
		for (WikiElement element: children) {
			ListItemElement currentItem = (ListItemElement)element;
			currentItemLevel = currentItem.getLevel();
			currentListLevel = lists.size()-1;
			// do we need to pop any old list(s)?
			while (currentItemLevel < currentListLevel) { // current item level < current list level
				lists.pop();
				currentListLevel--;
			}
			// do we need a new list?
			if (currentItemLevel > currentListLevel) { // current item level > current list level
				ListElement deeperList = new ListElement();
				// if there is an item in the current list add the new list to that
				// else add the new list to the current list
				if (lists.peek().getChildren().size() > 0) {
					lists.peek().getChildren().peek().addElement(deeperList);
				} else {
					lists.peek().addElement(deeperList);
				}
				lists.push(deeperList);
				currentListLevel++;
			}
			lists.peek().addElement(currentItem);
		}
	}

}
