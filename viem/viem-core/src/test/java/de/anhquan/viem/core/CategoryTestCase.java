package de.anhquan.viem.core;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.json.CategoryImporter;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.model.Category;

public class CategoryTestCase extends LocalServiceTestCase{

	private ProductDao productDao;
	private ProductCategoryRelationDao pcrDao;
	private CategoryDao categoryDao;
	private OptionDao optionDao;
	private OptionTypeDao optionTypeDao;


	@Before
	public void setUp(){
		basicSetUp();
		productDao = injector.getInstance(ProductDao.class);
		pcrDao = injector.getInstance(ProductCategoryRelationDao.class);
		categoryDao = injector.getInstance(CategoryDao.class);
		optionDao = injector.getInstance(OptionDao.class);
		optionTypeDao = injector.getInstance(OptionTypeDao.class);
		createDemoData();
	}
	
	private void createDemoData(){
		productDao.clearAll();
		pcrDao.clearAll();
		categoryDao.clearAll();

		//--------- Create Category
		Category vorspeisen = categoryDao.give("vorspeisen");
		Category suppen = categoryDao.give("suppen");
		Category ente = categoryDao.give("ente");
		Category hauptgerichte = categoryDao.give("hauptgerichte");

		categoryDao.put(vorspeisen);
		categoryDao.put(suppen);
		categoryDao.put(ente);
		
		
	}
	
	@After
	public void tearDown(){
		basicTearDown();
	}
	
	@Test
	public void testExport(){
		System.out.println(categoryDao.toJSONString());
	}
	
	@Test
	public void testImport(){
		productDao.clearAll();
		categoryDao.clearAll();
		optionDao.clearAll();
		pcrDao.clearAll();
		
		CategoryImporter importer = injector.getInstance(CategoryImporter.class);
		String content;
		try {
			content = new Scanner(new File("ProductCategoryAdminServlet.json")).useDelimiter("\\Z").next();
			importer.parse(content);
		} catch (FileNotFoundException | ParseJSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testFind(){
		
		
	}
	
	@Override
	protected void guiceRegister() {
		register(ProductDao.class);
		register(ProductCategoryRelationDao.class);
		register(CategoryDao.class);
	}

}
