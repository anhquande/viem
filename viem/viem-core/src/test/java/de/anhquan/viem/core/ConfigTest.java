package de.anhquan.viem.core;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.anhquan.config4j.ConfigItem;
import de.anhquan.viem.core.model.ConfigEntity;

public class ConfigTest extends LocalServiceTestCase{

	@Override
	protected void guiceRegister() {
		
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
	public void testConfig(){

//		List<String> keys = config.getKeys();
//
//		System.out.println("keys: "+keys);
//		
//		List<ConfigItem> items = config.getConfigItems();
//
//		for (ConfigItem configItem : items) {
//			System.out.println(configItem.getKey()+ " = " +configItem.getValue() + ". TITLE = "+configItem.getTitle());
//		}
//		System.out.println(items);
//		config.setOpenning(true);
//		System.out.println("is store openning " + config.getOpenning());
		
		List<ConfigEntity> entities = configDao.getAll();
		for (ConfigEntity configItem : entities) {
			System.out.println(configItem.getName() + " = "+ configItem.getValue());

		}
		
	}

}
