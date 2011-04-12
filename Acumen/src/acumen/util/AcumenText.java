package acumen.util;

import java.io.IOException;

import acumen.data.xml.XmlWriter;

public class AcumenText extends DataType {

	private String _text;

	public String getText () {
		return _text;
	}
	
	public String setText(String text) {
		_text = text;
		return _text;
	}

	public AcumenText (String text) {
		this.setText(text);
	}

	public AcumenText () {
		this("");
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.getText().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		int hash = "AcumenText".hashCode();
		hash = hash * 31 + this.getText().hashCode();
		return hash;
	}

	@Override
	public void toXml (XmlWriter writer) throws IOException {
		writer.writeRawText(this.getText());
	}

	@Override
	public String toString () {
		return this.getText();
	}

}
