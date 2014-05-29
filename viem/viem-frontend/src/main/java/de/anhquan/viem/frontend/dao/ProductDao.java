package de.anhquan.viem.frontend.dao;

import java.util.logging.Logger;

import de.anhquan.viem.core.dao.NameBasedDao;
import de.anhquan.viem.core.dao.objectify.OfyService;
import de.anhquan.viem.frontend.model.Product;

public class ProductDao extends NameBasedDao<Product>{

	static{
		OfyService.factory().register(Product.class);
	}
	
	public static final Logger log = Logger.getLogger(ProductDao.class.getName());
	
	protected ProductDao() {
		super(Product.class);
	}

}
