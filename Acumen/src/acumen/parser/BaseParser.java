package acumen.parser;

import java.io.IOException;

import acumen.data.xml.XmlWriter;

import acumen.parser.model.WikiElement;
import acumen.parser.scanner.ParseException;
import acumen.util.DataType;

public abstract class BaseParser extends DataType {
	
	private WikiElement _root;
	private WikiElement _current;
		
	public WikiElement getRoot () {
		return _root;
	}
	
	public WikiElement getCurrent () {
		return _current;
	}
	
	public WikiElement pushElement(WikiElement element) throws ParseException {
		WikiElement newCurrent = element;
		if (_root == null) {
			_root = element;
		} else {
			newCurrent = _current.addElement(element);
		}
		element.setParent(_current);
		_current = newCurrent;
		return element;
	}
	
	public WikiElement popElement() throws ParseException {
		WikiElement _result = _current;
		_current = _current.getParent();
		return _result;
	}
	
	public WikiElement pushSibling(WikiElement element) throws ParseException {
		this.popElement();
		this.pushElement(element);
		return element;
	}
	
	public WikiElement addChildElement(WikiElement element) {
		_current.getChildren().add(element);
		element.setParent(_current);
		return element;
	}
	
	@Override
	public String toString () {
		return _root.toString();
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		_root.toXml(xml);
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
}
