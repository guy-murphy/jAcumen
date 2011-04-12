package acumen.user.store;

import acumen.data.*;
import acumen.user.model.*;
import acumen.util.AcumenList;
import acumen.util.BinaryFunctor;
import acumen.util.Is;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserStore extends MySqlStoreBase {
	
	public static IUserCredentials readUser(ResultSet reader) throws StoreException {
		String uid = null;
		String name = null;
		String password = null;
		String maskRep = null;
		try {
			try {
				while(reader.next()) {
					uid = reader.getString("aus_code");
					name = reader.getString("aus_name");
					password = reader.getString("aus_password");
					maskRep = reader.getString("aus_mask");
				}
				
			} finally {
				closeReader(reader);
			}
			if (maskRep != null) {
				IUserCredentials credentials = new UserCredentials(uid, name, password, maskRep);
				return credentials;
			} else {
				return null;
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage());
		}
	}

	public MySqlUserStore (String server, String database, String user, String password) {
        super(server, database, user, password);
    }
	
	public boolean userExists (String uid) throws StoreException {
		return (this.getUser(uid) != null) ? true : false;
	}

	public boolean userExists (String name, String password) throws StoreException {
		return (this.getUser(name, password) != null) ? true : false;
	}
	
	public IUserCredentials getUser (String uid) throws StoreException {
		String sql = "select * from users where aus_code=?";
		ResultSet reader = this.read(sql, uid);
		return readUser(reader);
	}
	
	public IUserCredentials getUser (String userName, String userPassword) throws StoreException {
        String sql = "select * from users where aus_name=? and aus_password=?";
		ResultSet reader = this.read(sql, userName, userPassword);
		return readUser(reader);
	}
	
	public AcumenList<UserCredentials> getUsersByName (String userName) throws StoreException {
		AcumenList<UserCredentials> users = new AcumenList<UserCredentials>();
        String sql = "select * from users where aus_name=?";
		ResultSet reader = this.read(sql, userName);
		String uid = null;
		String name = null;
		String password = null;
		String maskRep = null;
		try {
			try {
				while (reader.next()) {
	                uid = reader.getString("aus_code");
	                name = reader.getString("aus_name");
	                password = reader.getString("aus_password");
	                maskRep = reader.getString("aus_mask");
					if (maskRep != null) {
						UserCredentials credentials = new UserCredentials(uid, name, password, maskRep);
						users.add(credentials);
					}
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage());
		}
		return users;
	}
	
	public AcumenList<UserCredentials> getUsersByRole (String role) throws StoreException {
		AcumenList<UserCredentials> users = new AcumenList<UserCredentials>();
		int rolePos = UserRole.roleIndex(role);
		if (rolePos != -1) {
            String sql = String.format("select * from users where substr(aus_mask, %s, 1) = '1'", Integer.toString(rolePos+1));
			ResultSet reader = this.read(sql);
			String uid = null;
			String name = null;
			String password = null;
			String maskRep = null;
			try {
				try {
					while (reader.next()) {
	                    uid = reader.getString("aus_code");
	                    name = reader.getString("aus_name");
	                    password = reader.getString("aus_password");
	                    maskRep = reader.getString("aus_mask");
						if (maskRep != null) {
							UserCredentials credentials = new UserCredentials(uid, name, password, maskRep);
							if (credentials.getRoles().getRole(role)) {
								users.add(credentials);
							}
						}
					}
				} finally {
					closeReader(reader);
				}
			} catch (SQLException err) {
				throw new StoreException(err.getMessage());
			}
		}
		return users;
	}
	
	public void addUser (IUserCredentials user) throws StoreException {
		this.addUser(user.getId(), user.getName(), user.getPassword(), user.getRoles().getMask());			
	}
	
	public void addUser (String uid, String name, String password, String mask) throws StoreException {
		if (!this.userExists(name, password)) {
			this.removeUser(uid);
            String sql = "insert into users (aus_code, aus_name, aus_password, aus_mask) values (?, ?, ?, ?)";
			this.exec(sql, uid, name, password, mask);
		} else {
			throw new StoreException("The username and password of the user you are trying to add already exist.");
		}
	}
	
	private void _alterGlobalRoles(String uid, long modifyingMask, BinaryFunctor<Long,Long,Long> alteration) throws StoreException {
		String sql = "select aus_mask from users where aus_code=?";
		ResultSet reader = this.read(sql, uid);
		String binMask = null;
		try {
			try {
				while (reader.next()) {
	                binMask = reader.getString("aus_mask");
				}
			} finally {
				closeReader(reader);
			}
		} catch (SQLException err) {
			throw new StoreException(err.getMessage());
		}
		if (Is.NotNullOrEmpty(binMask)) {
			long currentMask = Long.valueOf(binMask, 2);
			currentMask = alteration.invoke(currentMask, modifyingMask);
            sql = "update users set aus_mask=? where aus_code=?";
			this.exec(sql, Long.toBinaryString(currentMask), uid);
		}
	}
	
	public void addGlobalRoles (String uid, long modifyingMask) throws StoreException {
		_alterGlobalRoles(uid, modifyingMask, new BinaryFunctor<Long,Long,Long>(){
			public Long invoke(Long m1, Long m2) {
				return m1 | m2;
			}
		});
	}
	
	public void removeGlobalRoles (String uid, long modifyingMask) throws StoreException {
		_alterGlobalRoles(uid, modifyingMask, new BinaryFunctor<Long,Long,Long>(){
			public Long invoke(Long m1, Long m2) {
				return m1 &~ m2;
			}
		});
	}
	
	public void removeUser (String uid) throws StoreException {
        String sql = "delete from users where aus_code=?";
		this.exec(sql, uid);
	}

	public void removeUser (String name, String password) throws StoreException {
        String sql = "delete from users where aus_name=? and aus_password=?";
		this.exec(sql, name, password);
	}
	
	public void nukeEverythingInTheStore () throws StoreException {
        this.exec("truncate users");
    }
	
}
