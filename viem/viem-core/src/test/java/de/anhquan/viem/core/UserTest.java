package de.anhquan.viem.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserTest extends LocalServiceTestCase {

	@Test
	public void testIsAdmin() {
		UserService userService = UserServiceFactory.getUserService();
		assertTrue(userService.isUserAdmin());
	}

	@Override
	protected void guiceRegister() {
		// TODO Auto-generated method stub

	}

}
