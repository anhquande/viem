import java.util.ArrayList;
import java.util.List;

import de.anhquan.viem.core.model.NavigationItem;
import de.anhquan.viem.core.servlet.ServletPath;


public class TestAnnotation {

	@ServletPath(basePath="/admin/test")
	public class MyClass {
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(MyClass.class.getAnnotation(ServletPath.class));
	}

}
