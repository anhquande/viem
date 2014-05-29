package de.anhquan.viem.frontend.util;

import java.util.List;

import de.anhquan.viem.frontend.model.Page;

public class PageExporter {

	/**
	 * Export a list of pages to JSON String
	 */
	public static String exportJSONString(List<Page> pages) {

		StringBuilder str = new StringBuilder();
		str.append("[");
		int i=0;
		int len = pages.size();
		for (Page page : pages) {
			str.append(page.toJSON());
			i++;
			if (i<len)
				str.append(",");
		}
		str.append("]");
		return str.toString();
	}
}
