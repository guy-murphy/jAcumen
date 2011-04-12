/**
 * User: Guy J. Murphy
 * Date: Jan 14, 2010
 * Time: 2:22:57 PM
 * File: Test.java
 */
package acumen.parser.model;

import java.io.IOException;
import java.io.StringWriter;

import acumen.data.xml.XmlWriter;

import acumen.util.Is;
import acumen.util.StringUtil;

/**
 * Represents any type of valid (URL|Topic|Image) link
 * that can occur within a wiki.
 * 
 * @author Guy J. Murphy
 */
public class LinkElement extends WikiElement {

	private static final long serialVersionUID = 5024335089964116870L;
	
	private String _label;
	private String _link;
	private String _type;
	private String _original;
	private String _param;
	
	public LinkElement(String original) {
		super(original);
		_original = original;
		this.parseOriginal();
	}
	
	protected void parseOriginal () {
		_original = _original.trim(); // kill any whitespace on the ends
		_original = StringUtil.trimEndsBy(_original, 1); // remove the [ ] from the ends
		String[] parts = _original.split("\\|"); // break it down into it's parts
		
		switch (parts.length) {
			case 1:
				_link = parts[0];
				_inferType(_link);
				break;
			case 2:
				_label = parts[0];
				_link = parts[1];
				_inferType(_link);
				break;
			case 3:
				_label = parts[0];
				_param = parts[1];
				_link = parts[2];
				_inferType(_link);
				break;
		}
	}
	
	private String _inferType (String link) {
		boolean isUrl = link.matches("(http|https|ftp|ftps)://([^ \\t\\n\\r])+");
		boolean isImage = link.matches("([^ \t\r\n])+\\.(jpg|png|gif|jpeg|svg)");
		if (isImage) {
			_type = "image";
		} else if (isUrl) {
			_type = "url";
		} else {
			_type = "topic";
		}
		return _type;
	}
	
	public void setLink (String link) {
		_link = link;
	}
	
	public String getLink () {
		return _link;
	}
	
	public void setLabel(String label) {
		_label = label;
	}
	
	public String getLabel() {
		return Is.NotNullOrEmpty(_label) ? this.getLink() : _label;
	}
	
	public String getType () {
		return _type;
	}
	
	public void setType (String type) {
		_type = type;
	}
	
	public String getParam () {
		return _param;
	}
	
	public void setParam(String param) {
		_param = param;
	}
	
	@Override
	public void toString (StringWriter str, int indent) {
		indent(str, indent);
		str.append(String.format("(%s-link ", _type));
		if (Is.NotNullOrEmpty(_label)) {
			str.append(String.format("@label %s ", _label));
		}
		str.append(" - ");
		str.append(_link);
		str.append("\n)");
	}
	
	@Override
	public void toXml (XmlWriter xml) throws IOException {
		xml.writeEntity(String.format("%s-link", _type));
		if (Is.NotNullOrEmpty(_label)) {
			xml.writeAttribute("label", _label);
		}
		if (Is.NotNullOrEmpty(_param)) {
			xml.writeAttribute("param", _param);
		}
		xml.writeText(this.getLink());
		xml.endEntity();
	}

}
