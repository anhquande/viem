package de.anhquan.viem.core.dao;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.DefaultProduct;
import de.anhquan.viem.core.model.Option;

public class DefaultProductDao extends NameBasedDao<DefaultProduct>{

	static{
		ObjectifyService.register(DefaultProduct.class);
	}
	
	public static final Logger log = Logger.getLogger(DefaultProductDao.class.getName());
	
	OptionDao optionDao;
	ProductCategoryRelationDao pcRelDao;
	
	@Inject
	protected DefaultProductDao(OptionDao optionDao) {
		super(DefaultProduct.class);
		this.optionDao = optionDao;
	}

	public List<Option> getOptionsAsList() {
		// TODO Auto-generated method stub
		return null;
	}
    

}
