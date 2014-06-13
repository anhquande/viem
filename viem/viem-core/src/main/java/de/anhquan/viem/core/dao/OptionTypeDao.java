package de.anhquan.viem.core.dao;

import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.OptionType;

public class OptionTypeDao extends NameBasedDao<OptionType>{

	static{
        ObjectifyService.register(OptionType.class);
	}
	
	public static final Logger log = Logger.getLogger(OptionTypeDao.class.getName());
	
	protected OptionTypeDao() {
		super(OptionType.class);
	}
	

}
