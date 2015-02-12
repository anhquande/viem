package de.anhquan.viem.core.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Ref;

import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.Product;

public class ProductDao extends NameBasedDao<Product>{

	static{
		ObjectifyService.register(Product.class);
	}
	
	public static final Logger log = Logger.getLogger(ProductDao.class.getName());
	
	OptionDao optionDao;
	ProductCategoryRelationDao pcRelDao;
	
	@Inject
	protected ProductDao(OptionDao optionDao, ProductCategoryRelationDao pcRelDao) {
		super(Product.class);
		this.optionDao = optionDao;
		this.pcRelDao = pcRelDao;
	}

    public List<Product> getAll() {
        return ofy().load().type(clazz).order("sortValue").list();
    }
    
    private void selfClean(Product product){
    	if (product!=null){
    		//clear all associated options
    		List<Ref<Option>> options = product.getOptions();
    		for (Ref<Option> ref : options) {
				optionDao.delete(ref.get());
			}
    		
    		//remove itself from its categories
    		pcRelDao.deleteCategoryOf(product);
    	}
    }
    
    @Override
    public void delete(Long id) {
    	selfClean(get(id));
    	super.delete(id);
    }
    
    @Override
    public void delete(Product product) {
    	if (product==null)
			return;
		
    	selfClean(product);
    	super.delete(product);
    }

    @Override
    public void delete(List<Product> products) {
    	for (Product product : products) {
			selfClean(product);
		}
    	super.delete(products);
    }
}
