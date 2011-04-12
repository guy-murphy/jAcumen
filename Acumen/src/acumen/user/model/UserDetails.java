package acumen.user.model;

import acumen.util.AcumenDictionary;
import acumen.util.AcumenText;
import acumen.util.IDataType;

public class UserDetails extends AcumenDictionary<String, IDataType> implements IUserDetails {

	private static final long serialVersionUID = -4343356096977526960L;

	public UserDetails () {
		super();
		this.setLabel("user-details");
	}
	
	public UserDetails(IUserDetails users) {
		super(users);
		this.setLabel("user-details");
	}

	public void put (String key, String value) {
		super.put(key, new AcumenText(value));
	}

}
