package de.anhquan.viem.core.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Product;
import de.anhquan.viem.core.model.ProductCategoryRelation;

public class ProductCategoryRelationDao extends BaseDao<ProductCategoryRelation>{

	static{
		ObjectifyService.register(ProductCategoryRelation.class);
	}
	
	public static final Logger log = Logger.getLogger(ProductCategoryRelationDao.class.getName());
	
	protected ProductCategoryRelationDao() {
		super(ProductCategoryRelation.class);
	}

	public void addProductToCategory(Product product, Category target){
		List<ProductCategoryRelation> found = findRelations(product, target);
		if (found.isEmpty()){
			ProductCategoryRelation rel = new ProductCategoryRelation();
			rel.setProduct(product);
			rel.setCategory(target);
			put(rel);
			
			product.addCategory(target);
		}
	}
	
	public void deleteProductOf(Category category){
		List<ProductCategoryRelation> rels = findRelations(category);
		delete(rels);
	}
	
	public void deleteCategoryOf(Product product){
		product.clearCategories();
		List<ProductCategoryRelation> rels = findRelations(product);
		delete(rels);
	}
	
	private List<ProductCategoryRelation> findRelations(Product product) {
		if (product!=null)
			return listByProperty("product", product);
		else
			return new LinkedList<ProductCategoryRelation>();
	}

	private List<ProductCategoryRelation> findRelations(Category category){
		if (category!=null)
			return listByProperty("category", category);
		else
			return new LinkedList<ProductCategoryRelation>();
	}
	
	public List<ProductCategoryRelation> findRelations(Product product, Category category){
		return query().filter("product", product).filter("category", category).list();
	}

	public List<Category> findCategoryOf(Product product){
		List<Category> selectedCategories = new LinkedList<Category>();

		if (product!=null){
			List<ProductCategoryRelation> rels = query().filter("product", product).list();
			for (ProductCategoryRelation rel : rels) {
				selectedCategories.add(rel.getCategory());
			}
		}
		
		return selectedCategories;
	}

	public List<Product> findProductOf(Category category){
		List<Product> products = new LinkedList<Product>();

		if (category!=null){
			List<ProductCategoryRelation> rels = query().filter("category", category).list();
			for (ProductCategoryRelation rel : rels) {
				products.add(rel.getProduct());
			}
		}
		
		return products;
	}

}
