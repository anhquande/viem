package de.anhquan.viem.frontend.dao;

import java.util.logging.Logger;

import de.anhquan.viem.core.dao.NameBasedDao;
import de.anhquan.viem.core.dao.objectify.OfyService;
import de.anhquan.viem.frontend.model.ProductCategory;

public class ProductCategoryDao extends NameBasedDao<ProductCategory>{

	static{
		OfyService.factory().register(ProductCategory.class);
	}
	
	public static final Logger log = Logger.getLogger(ProductCategoryDao.class.getName());
	
	protected ProductCategoryDao() {
		super(ProductCategory.class);
	}

}
