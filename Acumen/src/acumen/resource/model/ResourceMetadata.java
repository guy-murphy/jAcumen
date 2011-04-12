package acumen.resource.model;

import java.util.Map;

import acumen.util.AcumenDictionary;
import acumen.util.Is;

public class ResourceMetadata extends AcumenDictionary<String,String> {

	private static final long serialVersionUID = -2455866462090917051L;
	
	public ResourceMetadata () {
		super();
		this.setLabel("meta-data");
	}
	
	public ResourceMetadata (String values) {
		this();
		this.fromString(values);
	}
	
	public void fromString (String values) {
		if (Is.NotNullOrEmpty(values)) {
			values = values.replace("||", "¬");
			String[] parts = values.split("[¬]");
			for (String part: parts) {
				String[] pair = part.split("[|]");
				if (pair.length == 2) {
					this.put(pair[0], pair[1]);
				}
			}
		}
	}
	
	public String toString () {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry: this.entrySet()) {
			if (sb.length() > 0) {
				sb.append("||");
			}
			sb.append(String.format("%s|%s", entry.getKey(), entry.getValue()));
		}
		return sb.toString();
	}

}
