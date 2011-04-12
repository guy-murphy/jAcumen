/**
 * User: Guy J. Murphy
 * Date: Apr 27, 2010
 * Time: 10:53:38 AM
 * File: IUserCredentials.java
 */
package acumen.user.model;

import acumen.util.IDataType;

/**
 * @author Guy J. Murphy
 */
public interface IUserCredentials extends IDataType {

	public abstract boolean isSameAs(IUserCredentials user);

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getId()
	 */
	public abstract String getId();

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setId(java.lang.String)
	 */
	public abstract void setId(String id);

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setName(java.lang.String)
	 */
	public abstract void setName(String name);

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getName()
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#getPassword()
	 */
	public abstract String getPassword();

	/* (non-Javadoc)
	 * @see acumen.user.model.IUserCredentials#setPassword(java.lang.String)
	 */
	public abstract void setPassword(String password);

	public abstract IUserDetails getUserDetails();

	public abstract void setUserDetails(IUserDetails details);

	public abstract UserRole getRoles();

	public abstract void setRoles(UserRole roles);

	public abstract boolean isOwner();

	public abstract boolean isAdmin();

	public abstract boolean isUser();

	public abstract boolean isDeveloper();

	public abstract boolean isModerator();

	public abstract boolean isTrusted();

	public abstract boolean isAnon();

}