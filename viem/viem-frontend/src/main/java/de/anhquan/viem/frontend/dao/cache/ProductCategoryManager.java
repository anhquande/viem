package de.anhquan.viem.frontend.dao.cache;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.CacheManager;
import de.anhquan.viem.frontend.dao.ProductCategoryDao;
import de.anhquan.viem.frontend.model.ProductCategory;

public class ProductCategoryManager extends CacheManager<ProductCategory> {

	
	@Inject
	protected ProductCategoryManager(ProductCategoryDao dao){
		this.dao = dao;
	}
	
	@Override
	public ProductCategory createObject() {
		return new ProductCategory();
	}

}
