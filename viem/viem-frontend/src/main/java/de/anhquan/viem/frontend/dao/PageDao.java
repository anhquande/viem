package de.anhquan.viem.frontend.dao;

import java.util.logging.Logger;

import de.anhquan.viem.core.dao.NameBasedDao;
import de.anhquan.viem.core.dao.objectify.OfyService;
import de.anhquan.viem.frontend.model.Page;

public class PageDao extends NameBasedDao<Page>{

	static{
		OfyService.factory().register(Page.class);
	}
	
	public static final Logger log = Logger.getLogger(PageDao.class.getName());
	
	protected PageDao() {
		super(Page.class);
	}

}
