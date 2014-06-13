package de.anhquan.viem.core.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.OptionItem;

public class OptionItemDao extends BaseDao<OptionItem>{

	static{
        ObjectifyService.register(OptionItem.class);
	}
	
	public static final Logger log = Logger.getLogger(OptionItemDao.class.getName());
	
	protected OptionItemDao() {
		super(OptionItem.class);
	}
	
    public List<OptionItem> getAll() {
        return ofy().load().type(clazz).order("sortValue").list();
    }
    
	public void sort(String input){
		log.info("Sort OptionItem "+input);
		if ((input==null) || (input.isEmpty()))
			return;
		
		String[] sortedList = input.split(",");
		int index = 0;
		log.info("sort count ..."+sortedList.length);
		for (String name : sortedList) {
			try{
				Long id = Long.parseLong(name);
				OptionItem entity = get(id);
				if (entity!=null){
					entity.setSortValue(index++);
					put(entity);
				}
			}
			catch(Exception e){}
		}
	}

}