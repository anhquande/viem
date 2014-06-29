package de.anhquan.viem.core;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.googlecode.objectify.Ref;

import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionItemDao;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.json.ProductImporter;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.OptionItem;
import de.anhquan.viem.core.model.OptionType;
import de.anhquan.viem.core.model.Product;
import de.anhquan.viem.core.model.ProductCategoryRelation;

public class ProductTestCase extends LocalServiceTestCase{

	private ProductDao productDao;
	private ProductCategoryRelationDao pcrDao;
	private CategoryDao categoryDao;
	private OptionDao optionDao;
	private OptionTypeDao optionTypeDao;
	private OptionItemDao optionItemDao;

	private void showAll(){
		System.out.println("--- Content of the Product Table ---");
		System.out.println("Number of items "+productDao.count());

		List<Product> products = productDao.getAll();
		for (Product p : products) {
			System.out.println(p.getName() + " = " + p.getTitle());
			System.out.println("OPTION COUNT>:"+p.getOptions().size());
			List<Ref<Option>> options = p.getOptions();
			for (Ref<Option> ref : options) {
				System.out.println(" ---> OPTION:"+ref.get().getTitle());
			}
		}
		System.out.println("--- // ---");
		
		System.out.println("--- Content of the Category Table ---");

		List<Category> categories = categoryDao.getAll();
		for (Category p : categories) {
			System.out.println(p.getName() + " = " + p.getTitle());
		}
		System.out.println("--- // ---");

		System.out.println("--- Content of the Relation Table ---");
		System.out.println("Number of items "+pcrDao.count());

		List<ProductCategoryRelation> rels = pcrDao.getAll();
		for (ProductCategoryRelation r : rels) {
			Product p = r.getProduct();
			Category c = r.getCategory();
			String pName= p == null ? "<NULL>" : p.getName();
			String cName= c == null ? "<NULL>" : c.getName();
			System.out.println(pName + " belongs to "+cName);
		}
		System.out.println("--- // ---");
		
		System.out.println("--- Content of the Option Table ---");

		List<Option> options = optionDao.getAll();
		System.out.println("Number of items: "+options.size());
		
		for (Option opt : options) {
			System.out.println(opt.getTitle());
		}
		System.out.println("--- // ---");

	}

	@Before
	public void setUp(){
		basicSetUp();
		productDao = injector.getInstance(ProductDao.class);
		pcrDao = injector.getInstance(ProductCategoryRelationDao.class);
		categoryDao = injector.getInstance(CategoryDao.class);
		optionDao = injector.getInstance(OptionDao.class);
		optionTypeDao = injector.getInstance(OptionTypeDao.class);
		optionItemDao = injector.getInstance(OptionItemDao.class);
		createDemoData();
	}
	
	private void createDemoData(){
		productDao.clearAll();
		pcrDao.clearAll();
		categoryDao.clearAll();

		//--------- Create Product
		Product p32 = productDao.give("32");
		p32.setTitle("Cha Gio Chay");
		p32.setBasePrice(200);
		
		Product p33 = productDao.give("33");
		p33.setTitle("Goi Cuon");
		p33.setBasePrice(250);
		
		Product p58 = productDao.give("58");
		p58.setTitle("Pho Vietnam");
		p58.setBasePrice(350);
		
		Product p109 = productDao.give("109");
		p109.setTitle("Vit Curry");
		p109.setBasePrice(950);
		
		productDao.put(p32);
		productDao.put(p33);
		productDao.put(p58);
		productDao.put(p109);
		
		//--------- Create Category
		Category vorspeisen = categoryDao.give("vorspeisen");
		Category suppen = categoryDao.give("suppen");
		Category ente = categoryDao.give("ente");
		Category hauptgerichte = categoryDao.give("hauptgerichte");

		categoryDao.put(vorspeisen);
		categoryDao.put(suppen);
		categoryDao.put(ente);
		
		//--------- Create relations
		pcrDao.addProductToCategory(p32, vorspeisen);
		pcrDao.addProductToCategory(p33, vorspeisen);
		pcrDao.addProductToCategory(p58, suppen);
		pcrDao.addProductToCategory(p109, ente);
		pcrDao.addProductToCategory(p109, hauptgerichte);
		
		// --------- Create Product Options
		OptionType t1 = new OptionType();
		t1.setName("sauce");
		t1.setTitle("Sauce ");
		optionTypeDao.put(t1);
		
		OptionType t2 = new OptionType();
		t2.setName("krauter");
		t2.setTitle("Krauter");
		optionTypeDao.put(t2);
		
		OptionType t3 = new OptionType();
		t3.setName("extras");
		t3.setTitle("Extras");
		optionTypeDao.put(t3);
		
		OptionType t4 = new OptionType();
		t4.setName("gratis-vospeisen");
		t4.setTitle("Gratis Vorspeisen");
		optionTypeDao.put(t4);
		
		Option opt1 = new Option();
		opt1.setTitle("Erdnuss");
		opt1.setType("sauce");
		optionDao.put(opt1);
		
		Option opt2 = new Option();
		opt2.setTitle("Süss-sauer");
		opt2.setType("sauce");
		optionDao.put(opt2);
		p33.addOption(opt1);
		p33.addOption(opt2);
		productDao.put(p33);
		
	}
	
	@After
	public void tearDown(){
		basicTearDown();
	}
	
	@Test
	public void testExport(){
		System.out.println(productDao.toJSONString());
	}
	
	@Test
	public void testImport(){
		productDao.clearAll();
		categoryDao.clearAll();
		optionDao.clearAll();
		pcrDao.clearAll();
		
		ProductImporter importer = injector.getInstance(ProductImporter.class);
		String content;
		try {
			content = new Scanner(new File("ProductAdminServlet.json")).useDelimiter("\\Z").next();
			importer.parse(content);
			
		} catch (FileNotFoundException | ParseJSONException e) {
			e.printStackTrace();
		}
		
		showAll();
		System.out.println(productDao.toJSONString());
	}
	
	@Test
	public void testDelete(){
		
		Product p = productDao.give("109");
		Option option = new Option();
		option.setTitle("sauce");
		optionDao.put(option);
		p.addOption(option);
		OptionItem item = new OptionItem();
		item.setSortValue(1);
		optionItemDao.put(item);
		option.addOptionItem(item );
		
		OptionItem item2 = new OptionItem();
		item2.setSortValue(2);
		optionItemDao.put(item2);
		option.addOptionItem(item2);

		OptionItem item3 = new OptionItem();
		item3.setSortValue(-1);
		optionItemDao.put(item3);
		option.addOptionItem(item3);

		testExport();
		Product found  = productDao.give("109");
		List<Ref<Option>> options = found.getOptions();
		for (Ref<Option> ref : options) {
			List<OptionItem> items = ref.get().getOptionItemsAsList();
			for (OptionItem optionItem : items) {
				System.out.println(optionItem);
			}
		}
		productDao.delete(p);

		System.out.println("After delete");
		testExport();
	}
	
	@Test
	public void testFind(){
		showAll();
		
		Product dup = new Product();
		dup.setName("109");
		dup.setTitle("VIT CURRY");
		productDao.put(dup);
		
		Product p109 = productDao.give("109");
		assertNotNull(p109);
		System.out.println("FOUND = "+p109.getTitle());
		
		List<Category> categories = pcrDao.findCategoryOf(p109);
		for (Category category : categories) {
			System.out.println(category.getName());
		}
		
		showAll();
	}
	
	@Test
	public void testProductOptions(){
		OptionType t1 = new OptionType();
		t1.setName("sauce");
		t1.setTitle("Sauce ");
		optionTypeDao.put(t1);
		
		OptionType t2 = new OptionType();
		t2.setName("krauter");
		t2.setTitle("Krauter");
		optionTypeDao.put(t2);
		
		OptionType t3 = new OptionType();
		t3.setName("extras");
		t3.setTitle("Extras");
		optionTypeDao.put(t3);
		
		OptionType t4 = new OptionType();
		t4.setName("gratis-vospeisen");
		t4.setTitle("Gratis Vorspeisen");
		optionTypeDao.put(t4);
		
		
		Product p32 = productDao.give("109");
		assertNotNull(p32);
		System.out.println("FOUND = "+p32.getTitle());

		productDao.put(p32);
		
		Product found = productDao.give("109");

		System.out.println("Options:");
		List<Ref<Option>> opts = found.getOptions();
		for (Ref<Option> ref : opts) {
			Option opt = ref.get();
			System.out.println(opt.getTitle());
		}
	}
	
	
	@Override
	protected void guiceRegister() {
		register(ProductDao.class);
		register(ProductCategoryRelationDao.class);
		register(CategoryDao.class);
	}

}
