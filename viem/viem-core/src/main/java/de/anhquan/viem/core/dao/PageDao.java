package de.anhquan.viem.core.dao;

import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.Page;

public class PageDao extends NameBasedDao<Page>{

	static{
		ObjectifyService.register(Page.class);
	}
	
	public static final Logger log = Logger.getLogger(PageDao.class.getName());
	
	protected PageDao() {
		super(Page.class);
	}

	/**
	 * Add a page
	 * Example: addPage("impressum","default","Impressum","Impressum","Add your impressum here");
	 * @param path
	 * @param template
	 * @param title
	 * @param description
	 * @param content
	 */
	public void addPage(String path, String template, String title, String description, String content){
		Page p = give(path);
		
		p.setTemplate(template);
		p.setTitle(title);
		p.setDescription(description);
		p.setContent(content);
		put(p);
	}
	
}
