package de.anhquan.viem.core;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.anhquan.viem.core.json.AppSettingImporter;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.model.AppSetting;

public class AppSettingTest extends LocalServiceTestCase{

	private void showAll(){
		System.out.println("--- Content of the AppSetting Table ---");
		System.out.println("Number of items "+appSettingDao.count());

		List<AppSetting> settings = appSettingDao.getAll();
		for (AppSetting appSetting : settings) {
			System.out.println(appSetting.getName() + " = " + appSetting.getValue());
		}
		System.out.println("--- // ---");
		System.out.println("Number of items "+appSettingDao.count());

	}

	@Before
	public void setUp(){
		basicSetUp();
	}
	
	@After
	public void tearDown(){
		basicTearDown();
	}
	
	@Test
	public void testFind(){
		appSettingDao.createDefaultSettings();
		AppSetting appTitle = appSettingDao.findByKey(AppSetting.APP_TITLE);
		assertEquals("Demo Shop", appTitle.getValue());

	}
	
	@Test
	public void testGetAndSet(){
		appSettingDao.createDefaultSettings();
		AppSetting appTitle = appSettingDao.findByKey(AppSetting.APP_TITLE);
		assertEquals("Demo Shop", appTitle.getValue());
		
		appTitle.setValue("Demo Shop 2");
		appSettingDao.put(appTitle);
		appTitle = appSettingDao.findByKey(AppSetting.APP_TITLE);
		assertEquals("Demo Shop 2", appTitle.getValue());
		
		appSettingDao.restoreDefaultSettings();
		appTitle = appSettingDao.findByKey(AppSetting.APP_TITLE);
		assertEquals("Demo Shop", appTitle.getValue());
	}
	
	@Test
	public void testAvoidDuplicate(){
		appSettingDao.clearAll();
		appSettingDao.createDefaultSettings();
		int count1 = appSettingDao.count();
		
		appSettingDao.createDefaultSettings();
		int count2 = appSettingDao.count();
		
		assertEquals(count1, count2);
		
		showAll();
	}
	
	@Test
	public void testClearAll(){
		appSettingDao.createDefaultSettings();
		System.out.println("--- After loading defaults ---");
		showAll();
		assertTrue(appSettingDao.count()>0);

		
		System.out.println("-----NOW clearAll -----");
		appSettingDao.clearAll();
		showAll();
		assertTrue(appSettingDao.count()==0);
	}
	
	@Test
	public void testImportSetting() throws FileNotFoundException,
			ParseJSONException {

		AppSettingImporter importer = new AppSettingImporter();

		String content = new Scanner(new File("settings.demo.json"))
				.useDelimiter("\\Z").next();
		System.out.println(content);
		List<AppSetting> settings = importer.parse(content);
		for (AppSetting appSetting : settings) {
			System.out.println(appSetting);
		}
	}

	@Override
	protected void guiceRegister() {
		// TODO Auto-generated method stub
		
	}

}
