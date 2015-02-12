package de.anhquan.viem.core.dao;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.ConfigEntity;

public class ConfigDao extends NameBasedDao<ConfigEntity>{

	static{
        ObjectifyService.register(ConfigEntity.class);
	}
	
	public ConfigDao() {
		super(ConfigEntity.class);
	}

}