package de.anhquan.viem.core.json;

import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.model.Category;

public class CategoryImporter extends BaseJSONImporter<Category>{

	private CategoryDao categoryDao;
	
	@Inject
	protected CategoryImporter(CategoryDao categoryDao){
		super(Category.class);
		this.categoryDao = categoryDao;
	}
	
	@Override
	public List<Category> parse(String strJSON) throws ParseJSONException {
		List<Category> categories = super.parse(strJSON);
		List<Category> savedCategories = new LinkedList<Category>();
		for(Category category : categories){
			savedCategories.add(save(category));
		}
		
		return savedCategories;
	}
	
	private Category save(Category category){
		
		Category found = categoryDao.findFirstItemWithName(category.getName());
		
		if (found!=null){
			found.copyFrom(category);
			categoryDao.put(found);
		}
		else
			found = categoryDao.put(category);
		
		List<Category> subCategories = found.getImportedSubCategories();
		
		if (subCategories!=null){
			for (Category sub : subCategories) {
				found.addSubCategory(save(sub));
			}
			found.clearImportedSubCategories();
			categoryDao.put(found);
		}
		return found;
	}

}
