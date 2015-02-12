package de.anhquan.viem.core.dao;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.Category;

public class CategoryDao extends NameBasedDao<Category>{

	static{
        ObjectifyService.register(Category.class);
	}
	
	public static final Logger log = Logger.getLogger(CategoryDao.class.getName());
	
	private ProductCategoryRelationDao pcRelDao;
	
	@Inject
	protected CategoryDao(ProductCategoryRelationDao pcRelDao) {
		super(Category.class);
		this.pcRelDao = pcRelDao;
	}
	
	@Override
	public void delete(Category object) {
		selfClean(object);
		super.delete(object);
	}
	
	@Override
	public void delete(List<Category> objects) {
		for (Category category : objects) {
			selfClean(category);
		}
		super.delete(objects);
	}
	
	@Override
	public void delete(Long id) {
		selfClean(get(id));
		super.delete(id);
	}
	
	@Override
	public void delete(String name) {
		selfClean(findFirstItemWithName(name));
		super.delete(name);
	}

	private void selfClean(Category category) {
		if (category!=null)
			pcRelDao.deleteProductOf(category);
	}

	public List<Category> getVisibleCategories() {
		List<Category> retList = new LinkedList<Category>();
		List<Category> all = getAll();
		for (Category category : all) {
			if (category.isVisible())
				retList.add(category);
		}
		
		Collections.sort(retList);
		return retList;
	}

}
