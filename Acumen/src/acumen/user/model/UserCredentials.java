package acumen.user.model;

import java.io.IOException;
import java.util.UUID;
import acumen.data.xml.XmlWriter;

import acumen.util.DataType;
import acumen.util.Is;

public class UserCredentials extends DataType implements IUserCredentials {

	private String _id;
	private String _name;
	private String _password;
	private UserRole _roles;
	private UserDetails _userDetails;
	
	public UserCredentials (IUserCredentials user) {
		this.setId(user.getId());
		this.setName(user.getName());
		this.setPassword(user.getPassword());
		this.getRoles().setFromMask(user.getRoles().getMask());
		if (_userDetails != null) {
			_userDetails.putAll(user.getUserDetails());
		}
	}
	
	public UserCredentials (String id) {
		this.setId(id);
	}
		
	public UserCredentials (String id, String name, String password, String maskRep) {
		this(id);
		this.setName(name);
		this.setPassword(password);
		this.getRoles().setFromMask(maskRep);
	}
	
	public UserCredentials (String id, String name, String password) {
		this(id, name, password, "0");
	}
	
	public UserCredentials (String name, String password) {
		this(UUID.randomUUID().toString(), name, password);
	}
	
	@Override
    public boolean equals (Object obj) {
    	if (this == obj) return true;
    	if (obj instanceof IUserCredentials && obj != null) {
    		IUserCredentials other = (IUserCredentials)obj;
    		return this.getId() == null ? other.getId() == null : this.getId().equals(other.getId());
    	}
    	return false;
    }
    
    @Override
    public int hashCode () {
    	int hash = 1;
    	hash = hash * 31 + "UserCredentials::".hashCode();
    	hash = hash * 31 + (this.getId() == null ? 0 : this.getId().hashCode());
    	return hash;
    }
    
    /* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isSameAs(acumen.user.model.IUserCredentials)
	 */
    public boolean isSameAs(IUserCredentials user) {
    	// compares id, name, password, and mask
    	// DOES NOT compare additional user details
    	return (this.equals(user) && this.getName().equals(user.getName()) && this.getPassword().equals(user.getPassword()) && this.getRoles().getMask().equals(user.getRoles().getMask()));
    }

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getId()
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getId()
	 */
	public String getId() {
		if (Is.NullOrEmpty(_id)) {
			_id = "anon";
		}
		return _id;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setId(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setId(java.lang.String)
	 */
	public void setId(String id) {
		_id = id;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setName(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setName(java.lang.String)
	 */
	public void setName(String name) {
		_name = name;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getName()
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getName()
	 */
	public String getName() {
		return _name;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getPassword()
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getPassword()
	 */
	public String getPassword() {
		if (Is.NullOrEmpty(_password)) {
			_password = "anon";
		}
		return _password;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setPassword(java.lang.String)
	 */
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setPassword(java.lang.String)
	 */
	public void setPassword(String password) {
		_password = password;
	}

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getUserDetails()
	 */
	public IUserDetails getUserDetails() {
		if (_userDetails == null) {
			_userDetails = new UserDetails();
		}
		return _userDetails;
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setUserDetails(acumen.user.model.IUserDetails)
	 */
	public void setUserDetails (IUserDetails details) {
		_userDetails = new UserDetails(details);
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getRoles()
	 */
	public UserRole getRoles () {
		if (_roles == null) {
			_roles = new UserRole();
		}
		return _roles;
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setRoles(acumen.user.model.UserRole)
	 */
	public void setRoles (UserRole roles) {
		_roles = roles;
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isOwner()
	 */
	public boolean isOwner () {
		return this.getRoles().getRole("Owner");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isAdmin()
	 */
	public boolean isAdmin () {
		return this.getRoles().getRole("Admin");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isUser()
	 */
	public boolean isUser () {
		return this.getRoles().getRole("User");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isDeveloper()
	 */
	public boolean isDeveloper () {
		return this.getRoles().getRole("Developer");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isModerator()
	 */
	public boolean isModerator () {
		return this.getRoles().getRole("Moderator");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isTursted()
	 */
	public boolean isTrusted () {
		return this.getRoles().getRole("Trusted");
	}
	
	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#isAnon()
	 */
	public boolean isAnon () {
		return this.getRoles().isAnon();
	}
	
	@Override
	public void toXml(XmlWriter xml) throws IOException {
		xml.writeEntity("user-credentials");
		xml.writeAttribute("id", this.getId());
		xml.writeAttribute("name", this.getName());
		xml.writeAttribute("password", this.getPassword());
		xml.writeAttribute("mask", this.getRoles().getMask());
		xml.writeAttribute("mask-value", this.getRoles().getMask());
		if (this.getUserDetails() != null) {
			this.getUserDetails().toXml(xml);
		}
		xml.endEntity();
	}

}
