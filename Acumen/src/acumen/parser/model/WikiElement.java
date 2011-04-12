package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import acumen.data.xml.XmlWriter;

import acumen.util.*;
import acumen.parser.scanner.ParseException;
import acumen.parser.scanner.Token;

public class WikiElement extends DataType implements java.io.Serializable {

	private static final long serialVersionUID = 4674698177504017831L;
	
	public static void indent (StringWriter str, int indent) {
		// do something other than this
		// that is more efficient
		for (int i = 0; i < indent; i++) {
			str.append(" ");
		}
	}
	
	private String _original;
	private AcumenList<WikiElement> _children;
	private WikiElement _parent;
	
	public WikiElement (String original) {
		_original = original;
	}	
	@Override
	public boolean equals(Object obj) {
		if (obj != null) {
			if (obj instanceof WikiElement) {
				WikiElement token = (WikiElement)obj;
				return this.equals(token);
			} else if (obj instanceof Token) {
				Token token = (Token)obj;
				return this.equals(token);
			}
		}
		return false;
	}
	
	public boolean equals (WikiElement element) {
		return _original.equals(element.getOriginal());
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + _original.hashCode();
		return hash;
	}
	

	public AcumenList<WikiElement> getChildren () {
		if (_children == null) {
			_children = new AcumenList<WikiElement>();
		}
		return _children;
	}
	
	protected void setChildren (List<WikiElement> children) {
		for (WikiElement item: children) {
			this.getChildren().add(item);
		}
	}
	
	protected void clearChildren() {
		this.getChildren().clear();
	}
	
	public WikiElement getParent () {
		return _parent;
	}
	
	public WikiElement setParent (WikiElement element) {
		_parent = element;
		return element;
	}
	
	public String getOriginal () {
		return _original;
	}
	
	// This method is intended to be overriden
	// if there is to be any custom parsing/processing
	// of the element that has been added.
	public WikiElement addElement (WikiElement element) throws ParseException {
		this.getChildren().push(element);
		return element;
	}
	
	public static String toString (WikiElement token, int indent) throws IOException {
        StringWriter str = new StringWriter();
        try {
            token.toString(str, indent);
            return str.toString();
        } finally {
            str.close();
        }
    }
	
	public void finalise () throws ParseException {
		// do nothing for most cases
	}
	
	@Override
	public String toString () {
		try {
			return WikiElement.toString(this, 0);
		} catch (Exception err) {
			return err.getMessage();
		}
    }
	
	protected void childrenToString (StringWriter str, int indent) {
		if (_children != null && _children.size() > 0) {
			for (WikiElement element: _children) {
				element.toString(str, indent + 2);
			}
		}
	}
	
	public void toString (StringWriter str, int indent) {
		if (_original != null) {
			indent(str, indent);
			str.append("(WikiElement -");
			str.append(_original);
			this.childrenToString(str, indent);
			indent(str, indent);
			str.append(")\n");
		} else {
			indent(str, indent);
			str.append("(WikiElement)\n");
		}
	}
	
	public void childrenToXml (XmlWriter xml) throws IOException {
		if (_children != null) {
			for (WikiElement element: _children) {
				element.toXml(xml);
			}
		}
	}
	
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity("wiki-element");
		this.childrenToXml(xml);
		xml.endEntity();
	}

}
