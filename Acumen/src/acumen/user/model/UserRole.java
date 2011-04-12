/**
 * User: Guy J. Murphy
 * Date: Apr 26, 2010
 * Time: 4:30:54 PM
 * File: UserRole.java
 */
package acumen.user.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Guy J. Murphy
 */
public class UserRole {

	private static String[]		_roles;
	private boolean[]			_values;
	
	public static int roleIndex (String role) {
		for (int i = 0; i < 64; i++) {
			if (_roles[i].equals(role)) {
				return i;
			}
		}
		return -1;
	}
	
	public static String getRole(int index) {
		return _roles[index];
	}

	public UserRole() {
		if (_roles == null) {
			_roles = new String[] {
					"Owner", "Admin", "User", 
					"Developer", "Unactivated", 
					"SalesRep", "PakRep", "Moderator", 
					"Trusted", "SalesManager", 
					"TOManager", "TelemarketingManager",
					"Telemarketer", "XLResortsOwner"
			};
		}
		_values = new boolean[64];
		for (int i = 0; i < 64; i++) {
			_values[i] = false;
		}
	}

	public UserRole(String mask) {
		this.setFromMask(mask);
	}
	
	public UserRole(UserRole roles) {
		this.setFromMask(roles.getMask());
	}
	
	public boolean getValue(int index) {
		return _values[index];
	}

	public String getMask() {
		StringBuilder str = new StringBuilder();
		boolean hasValue = false;
		for (int i = 0; i < 64; i++) {
			if (_values[i]) {
				str.append('1');
				hasValue = true;
			} else {
				str.append('0');
			}
		}
		if (hasValue) {
			return str.toString();
		} else {
			return "0";
		}
	}
	
	public String[] getRoles () {
		ArrayList<String> roles = new ArrayList<String>();
		for (int i = 0; i < 64; i++) {
			if (_values[i]) {
				roles.add(_roles[i]);
			}
		}
		if (roles.size() == 0) {
			return new String[] { "Anon" };
		} else {
			String[] result = new String[roles.size()];
			roles.toArray(result);
			return result;
		}
	}
	
	public boolean getRole(String role) {
		if (role.equals("Anon")) {
			return this.isAnon();
		} else {
			return this.getValue(UserRole.roleIndex(role));
		}
	}
	
	public boolean isAnon () {
		for (int i = 0; i < 64; i++) {
			if (_values[i]) {
				return false;
			}
		}
		return true;
	}
	
	public UserRole setFromMask (String mask) {
		char[] chars = mask.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			_values[i] = (chars[i] == '1');
		}
		return this;
	}
	
	public UserRole setRole(String role, boolean value) {
		int index = UserRole.roleIndex(role);
		if (index == -1) {
			throw new IllegalArgumentException(String.format("The role '%s' does not exist.", role));
		} else {
			_values[index] = value;
		}
		return this;
	}
	
	public UserRole addRole(String role) {
		return this.setRole(role, true);
	}
	
	public UserRole addRoles(Collection<String> roles) {
		for (String role: roles) {
			this.addRole(role);
		}
		return this;
	}
	
	public UserRole addRoles(String ... roles) {
		return this.addRoles(Arrays.asList(roles));
	}
	
	public UserRole removeRole(String role) {
		return this.setRole(role, false);
	}
	
	public UserRole toggleRole(String role) {
		int index = UserRole.roleIndex(role);
		if (index == -1) {
			throw new IllegalArgumentException(String.format("The role '%s' does not exist.", role));
		} else {
			_values[index] = !_values[index];
		}
		return this;
	}
	
	public boolean hasRoles(Collection<String> roles) {
		for (String role: roles) {
			if (!role.equals("Anon") && !this.getRole(role)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean hasRoles(String ... roles) {
		return this.hasRoles(Arrays.asList(roles));
	}
	
	public boolean hasAnyRole(Collection<String> roles) {
		for (String role: roles) {
			if (role.equals("Anon") || this.getRole(role)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasAnyRole(String ... roles) {
		return this.hasAnyRole(Arrays.asList(roles));
	}


}
