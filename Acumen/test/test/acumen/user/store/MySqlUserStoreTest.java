package test.acumen.user.store;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.*;

import acumen.data.StoreException;
import acumen.user.store.MySqlUserStore;
import acumen.user.model.*;

public class MySqlUserStoreTest {
	
	private MySqlUserStore _read;
	private MySqlUserStore _write;
	
	private void _reset() throws StoreException {
		// reset the read db
        if (_read != null) {
            if (_read.isOpen()) {
            	_read.stop();
            }
        }
        _read = new MySqlUserStore("leviathan.acumen.es", "acumen_read", "root", "redacted");
        // reset the write db
        if (_write != null) {
            if (_write.isOpen()) {
            	_write.stop();
            }
        }
        _write = new MySqlUserStore("leviathan.acumen.es", "acumen_write", "root", "redacted");
        try {
        	_write.start();
        	_write.nukeEverythingInTheStore();
        } finally {
        	_write.stop();
        }
    }
	
	public MySqlUserStoreTest () throws StoreException {
        _reset();
    }
	
	@Test
	public void userExists () throws StoreException {
		_reset();
		try {
			_read.start();
			assertTrue(_read.userExists("a63c84e9-dd65-4d44-aaf7-54c1f885eac3"));
			assertTrue(_read.userExists("Guy Murphy", "redacted"));
			assertTrue(_read.userExists("b87a8c91-1a1d-4e19-b292-150120144bf4"));
			assertFalse(_read.userExists("xxx"));
			assertFalse(_read.userExists("somebody","xxx"));
		} finally {
			_read.stop();
		}
	}
	
	@Test
	public void readWriteUser () throws StoreException {
		_reset();
		try {
			_write.start();
			// add the users
			IUserCredentials u1_1 = new UserCredentials("u1", "User One", "p1");
			u1_1.getRoles().addRoles("Developer", "Moderator", "Trusted");
			IUserCredentials u2_1 = new UserCredentials("u2", "User Two", "p2");
			u2_1.getRoles().addRoles("Admin", "Developer", "Moderator", "Trusted");
			_write.addUser(u1_1);
			_write.addUser(u2_1);
			// read the users
			IUserCredentials u1_2 = _write.getUser("u1");
			IUserCredentials u2_2 = _write.getUser("User Two", "p2");
			// check that we've got back what we added
			assertTrue(u1_1.isSameAs(u1_2));
			//assertTrue(u2_1.isSameAs(u2_2));
		} finally {
			_write.stop();
		}
	}
	
	@Test
	public void testUserRoles () throws StoreException, IOException {
		_reset();
		try {
			
			UserRole roles = new UserRole();
			roles.addRoles("Owner", "User", "Admin", "Moderator", "Trusted", "TelemarketingManager");
			System.out.println(roles.getMask());
			
			_read.start();
			String uid = "a63c84e9-dd65-4d44-aaf7-54c1f885eac3";
			IUserCredentials user = _read.getUser(uid);
			String[] actualRoles = user.getRoles().getRoles();
			String[] expectedRoles = new String[] {"Owner", "Admin", "User", "Developer", "Moderator", "Trusted", "TelemarketingManager", };
			
			for(int i = 0; i < 6; i++) {
				assertEquals(expectedRoles[i], actualRoles[i]);
			}
			
		} finally {
			_read.stop();			
		}
		
	}
	
	@Test
	public void hasRoles () throws StoreException, IOException {
		_reset();
		try {
			_read.start();
			String uid = "a63c84e9-dd65-4d44-aaf7-54c1f885eac3";
			IUserCredentials user = _read.getUser(uid);
			user.setPrettyPrint(true);
			System.out.println(user.toXml());

			assertTrue(user.getRoles().hasRoles("Owner", "Admin"));
			assertFalse(user.getRoles().hasRoles("Admin", "Moderator", "Telemarketer"));
			assertTrue(user.getRoles().hasAnyRole("PakRep", "TOManager", "TelemarketingManager"));
			
		} finally {
			_read.stop();			
		}
	}

}
