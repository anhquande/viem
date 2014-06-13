package de.anhquan.viem.core.json;

import java.util.List;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionItemDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.OptionItem;
import de.anhquan.viem.core.model.Product;

public class ProductImporter extends BaseJSONImporter<Product>{

	 ProductDao entityDao;
	 ProductCategoryRelationDao pcRelDao;
	 CategoryDao categoryDao;
	 OptionDao optionDao;
	 OptionItemDao optionItemDao;
	 
	 @Inject
	public ProductImporter(ProductDao entityDao, ProductCategoryRelationDao pcRelDao, CategoryDao categoryDao, OptionDao optionDao, OptionItemDao optionItemDao){
		super(Product.class);
		this.entityDao = entityDao;
		this.pcRelDao = pcRelDao;
		this.categoryDao = categoryDao;
		this.optionDao = optionDao;
		this.optionItemDao = optionItemDao;
	}
	
	@Override
	public List<Product> parse(String strJSON) throws ParseJSONException {
		List<Product> products = super.parse(strJSON);
		
		for(Product product : products){
			Product foundProduct = entityDao.findFirstItemWithName(product.getName());
			if (foundProduct!=null){
				product.setId(null);
				foundProduct.copyFrom(product);
				product = entityDao.put(foundProduct);
			}
			else
				entityDao.put(product);
			
			pcRelDao.deleteCategoryOf(product);
			List<Category> categories = product.getImportedCategories();

			for (Category category : categories) {
				Category found = categoryDao.findFirstItemWithName(category.getName());
				if (found==null){
					category = categoryDao.put(category);
				}
				else{
					category = found;
				}
				pcRelDao.addProductToCategory(product, category);
			}
			
			List<Option> options = product.getImportedOptions();
			for (Option opt : options) {
				Option newOption = optionDao.put(opt);
				List<OptionItem> optionItems = opt.getImportedOptionItems();
				for(OptionItem optionItem : optionItems){
					optionItemDao.put(optionItem);
					newOption.addOptionItem(optionItem);
				}
				optionDao.put(newOption);
				product.addOption(newOption);
			}
			entityDao.put(product);
		}
		
		return products;
	}
}
