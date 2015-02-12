package de.anhquan.viem.core.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationItem {

	String url;
	String title;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public static NavigationItem create(String str) {
		NavigationItem item = null;
		String[] n = str.split(";",2);
		if (n.length==2){
			item = new NavigationItem();
			item.setUrl(n[0]);
			item.setTitle(n[1]);
		}	
	
		return item;
	}
	public static List<NavigationItem> createList(String input) {
		List<NavigationItem> sidebarList = new ArrayList<NavigationItem>();
		
		if ((input != null) && (!input.isEmpty())){
			String[] strList = input.split("\\|");

			for (String str : strList) {
				NavigationItem item = NavigationItem.create(str);
				if (item!=null)
					sidebarList.add(item);
			}
		}

		return sidebarList;
	}
	
	
}
