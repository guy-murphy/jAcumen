package test.acumen.user.model;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

import org.junit.*;

import acumen.user.model.*;

import java.io.IOException;

/**
 * User: gmurphy
 * Date: 06-Oct-2009
 * Time: 15:55:29
 */
public class UserCredentialsTest {
	
	@Test
	public void constructor () {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		
		assertEquals(user.getId(), "user1");
		assertEquals(user.getName(), "Guy Murphy");
		assertEquals(user.getPassword(), "xxx");
		assertEquals("0", user.getRoles().getMask());
	}
	
	@Test
	public void mask () throws IOException {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		user.getRoles().addRoles("Admin", "Moderator");			
		assertTrue(user.isAdmin());
		assertFalse(user.isOwner());
		assertTrue(user.getRoles().hasRoles("Admin", "Moderator"));
	}
	
	@Test
	public void details () throws IOException {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		user.getUserDetails().put("department", "IT");
		user.getUserDetails().put("job", "programmer");
		assertEquals(user.getUserDetails().get("department"), "IT");
		assertEquals(user.getUserDetails().get("job"), "programmer");
		user.setPrettyPrint(true);
		System.out.println(user.toXml());
	}
	
	@Test
	public void getRoles () {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		user.getRoles().addRoles("Admin", "Owner", "Developer");			
		
		String[] actualRoles = user.getRoles().getRoles();
		String[] expectedRoles = new String[] {"Owner", "Admin", "Developer"};
		
		for(int i = 0; i < expectedRoles.length; i++) {
			assertEquals(expectedRoles[i], actualRoles[i]);
		}
	}
		
	@Test
	public void hasRoles () throws IOException {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		user.getRoles().addRoles("Admin", "Moderator");			
		
		assertTrue(user.getRoles().hasRoles("Admin", "Moderator"));
		assertTrue(user.isAdmin());
		assertFalse(user.getRoles().hasRoles("Admin", "Owner"));
	}
	
	@Test
	public void hasAnyRoles () {
		UserCredentials user = new UserCredentials("user1", "Guy Murphy", "xxx");
		user.getRoles().addRoles("Admin", "Moderator", "Owner", "Developer", "Trusted");
		assertTrue(user.getRoles().hasAnyRole("User","Moderator"));
	}


}
