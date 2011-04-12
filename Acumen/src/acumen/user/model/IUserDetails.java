package acumen.user.model;

import acumen.util.IAcumenDictionary;
import acumen.util.IDataType;

public interface IUserDetails extends IAcumenDictionary<String,IDataType> {
	public void put (String key, String value);
}