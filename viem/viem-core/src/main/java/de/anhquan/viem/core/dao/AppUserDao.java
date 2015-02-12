package de.anhquan.viem.core.dao;

import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.AppUser;

public class AppUserDao extends NameBasedDao<AppUser> {

	protected AppUserDao() {
		super(AppUser.class);
	}

	static {
        ObjectifyService.register(AppUser.class);
    }

	public static final Logger log = Logger.getLogger(AppUserDao.class.getName());
	
}