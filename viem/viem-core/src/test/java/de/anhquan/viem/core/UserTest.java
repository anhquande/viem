package de.anhquan.viem.core;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.anhquan.viem.core.json.AppSettingImporter;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.model.AppSetting;

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
