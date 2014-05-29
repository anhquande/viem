package de.anhquan.viem.frontend.dao.cache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.CacheManager;
import de.anhquan.viem.frontend.dao.ProductDao;
import de.anhquan.viem.frontend.model.Product;

public class ProductManager extends CacheManager<Product> {

	
	@Inject
	protected ProductManager(ProductDao dao){
		this.dao = dao;
	}
	
	@Override
	public Product createObject() {
		return new Product();
	}
	
	public List<Product> findByCategory(String category){
		
		List<Product> founds = new LinkedList<Product>();
		List<Product> products = this.getAll();
		for (Product product : products) {
			if (product.belongsTo(category)){
				founds.add(product);
			}
		}
		
		Collections.sort(founds);
		return founds;
	}

}
