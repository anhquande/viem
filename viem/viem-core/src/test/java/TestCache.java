import java.util.ArrayList;
import java.util.List;

import de.anhquan.viem.core.dao.cache.CacheManager;
import de.anhquan.viem.core.model.AppSetting;
import de.anhquan.viem.core.model.NavigationItem;


public class TestCache {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CacheManager<AppSetting> cache = new CacheManager<AppSetting>() {
			
			@Override
			public AppSetting createObject() {
				return new AppSetting();
			}
		};

		AppSetting e1 = new AppSetting();
		e1.setName("e1");
		cache.put(e1 );
		
		AppSetting e2 = new AppSetting();
		e2.setName("e2");
		cache.put(e2);
		
		AppSetting e3 = new AppSetting();
		e3.setName("e3");
		cache.put(e3);
		
		AppSetting e4 = new AppSetting();
		e4.setName("e4");
		cache.put(e4);
		List<AppSetting> list = cache.getAll();
		System.out.println("Cache before delete");
		for (AppSetting appSetting : list) {
			System.out.println(appSetting.getName());
		}
		
		System.out.println("\n------------ Test delete ");
		cache.delete(e4);
		list = cache.getAll();
		System.out.println("ok! now cache is");
		for (AppSetting appSetting : list) {
			System.out.println(appSetting.getName());
		}
	}
}
