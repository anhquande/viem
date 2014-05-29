package de.anhquan.viem.core.dao;

import java.util.List;
import java.util.logging.Logger;

import de.anhquan.viem.core.model.AppSetting;

public class AppSettingDao extends NameBasedDao<AppSetting> {

	public static final Logger log = Logger.getLogger(AppSettingDao.class.getName());
	
	protected AppSettingDao() {
		super(AppSetting.class);
		log.info("init AppSettingDao...");
	}

	public AppSetting findByKey(String name){
		List<AppSetting> entities = ofy().query(clazz).filter("name", name).list();
		if ((entities == null) || (entities.isEmpty()))
			return null;
		return entities.get(0);
	}
	
	public String getSettingValue(String name){
		List<AppSetting> entities = ofy().query(clazz).filter("name", name).list();
		if ((entities == null) || (entities.isEmpty()))
			return "";
		return entities.get(0).getValue();
	}
		
	public List<AppSetting> getAll() {
        //return ofy().query(this.clazz).order("group").order("sortValue").list();
		//TODO : cannot sort by group on GoogleAppEngine
		return ofy().query(this.clazz).list();
    }
	
}