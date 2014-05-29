import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import de.anhquan.viem.core.dao.cache.CacheManager;
import de.anhquan.viem.frontend.dao.ProductDao;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.model.Product;
import de.anhquan.viem.frontend.servlet.ProductAdminServlet;


public class ProductTest {
	
	@Test
	public void testProduct(){
		
		testSingle("speise;vorspeisen;angebote;deal of the week");
		testSingle(null);
		testSingle("angebote");

	}
	
	private void testSingle(String input){
		System.out.println("Input = "+input);
		List<String> list;
		Product p = new Product();

		p.parseProductCategories(input);
		System.out.println("in database:");
		System.out.println(p.getProductCategories());
		
		System.out.println("as List:");

		list = p.getProductCategoriesAsList();
		for (String str : list) {
			System.out.println(str);
		}
		System.out.println("--------------");
	}
	
	class MockProductManager extends ProductManager{
		public MockProductManager() {
			super(null);
		}

		@Override
		public Product createObject() {
			return new Product();
		}
	}
	
	public static void main(String[] args){
		ProductTest test= new ProductTest();
		test.testImport();
	}
	
	public void testImport(){
		MockProductManager manager = new MockProductManager();
		
		ProductAdminServlet servlet = new ProductAdminServlet(null, manager , null, null );
		
		String uploadedContent;
		try {
			uploadedContent = new Scanner(new File("ProductAdminServlet.demo.json")).useDelimiter("\\Z").next();
			System.out.println(uploadedContent);
			servlet.onProcessFileUploadSuccess(uploadedContent);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
