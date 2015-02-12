package de.anhquan.viem.core.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.OptionItem;

public class OptionDao extends BaseDao<Option>{

	static{
        ObjectifyService.register(Option.class);
	}
	
	public static final Logger log = Logger.getLogger(OptionDao.class.getName());
	
	private OptionItemDao optionItemDao;
	
	@Inject
	protected OptionDao(OptionItemDao optionItemDao) {
		super(Option.class);
		this.optionItemDao = optionItemDao;
	}
	
    public List<Option> getAll() {
        return ofy().load().type(clazz).order("sortValue").list();
    }
    
	public void sort(String input){
		if ((input==null) || (input.isEmpty()))
			return;
		
		String[] sortedList = input.split(",");
		int index = 0;
		for (String name : sortedList) {
			try{
				Long id = Long.parseLong(name);
				Option entity = get(id);
				if (entity!=null){
					entity.setSortValue(index++);
					put(entity);
				}
			}
			catch(Exception e){}
		}
	}

	@Override
	public void delete(List<Option> objects) {
		for (Option option : objects) {
			selfClean(option);
		}
		super.delete(objects);
	}
   
	@Override
	public void delete(Long id) {
		selfClean(get(id));
		super.delete(id);
	}
	
	@Override
	public void delete(Option object) {
		if (object==null)
			return;
		
		selfClean(object);
		super.delete(object);
	}

	private void selfClean(Option option){
		if (option!=null){
			List<OptionItem> items = option.getOptionItemsAsList();
			for (OptionItem item : items) {
				optionItemDao.delete(item);
			}
			option.setOptionItems(null);
		}
	}
}
