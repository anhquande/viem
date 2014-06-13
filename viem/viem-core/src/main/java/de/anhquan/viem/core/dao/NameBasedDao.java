package de.anhquan.viem.core.dao;

import java.util.List;
import java.util.logging.Logger;

import de.anhquan.viem.core.model.NameBasedEntity;

public abstract class NameBasedDao<T extends NameBasedEntity> extends BaseDao<T> {

	public static final Logger log = Logger.getLogger(NameBasedDao.class.getName());

	protected NameBasedDao(final Class<T> clazz) {
        super(clazz);
    }
	
	/**
	 * Sort items
	 * @param sortedIds Comma-separated list of entity ID
	 */
	public void sort(String sortedIds){
		if ((sortedIds==null) || (sortedIds.isEmpty()))
			return;
		
		String[] ids = sortedIds.split(",");
		int index = 0;
		for (String strId : ids) {
			try{
				Long id = Long.parseLong(strId);
				T entity = get(id);
				if (entity!=null){
					entity.setSortValue(index++);
					put(entity);
				}
			}catch (NumberFormatException e) {
				log.info("Cannot parse ID from String");
			}
		}
	}
	
	public Integer count() {
		return query().count();
	}

	public List<T> getItemsHasName(String name){
		return listByProperty("name", name);
	}
	
	public T findFirstItemWithName(String name) {
		List<T> founds = getItemsHasName(name);
		if (founds.isEmpty())
			return null;
		return founds.get(0);
	}
	
	public List<T> getItemsHasNameStartsWith(String prefix) {
		return query().filter("name >=", prefix).filter("name <",prefix + "\uFFFD").list();
	}
	
	public String getEntityClassName(){
		return clazz.getSimpleName();
	}
	
	/**
	 * Delete Entity by Name
	 * @param name
	 */
	public void delete(String name){
		T found = findFirstItemWithName(name);
		if (found!=null)
			delete(found);
	}
	
	private T newEntity(String name){
		
		try {
			T entity = (T)clazz.newInstance();
			
			entity.setName(name);
			return put(entity);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public T give(String name){
		List<T> founds = listByProperty("name", name);
		if (founds.isEmpty())
			return newEntity(name);
		
		return founds.get(0);
	}
	
	public T give(){
		try {
			T entity = (T)clazz.newInstance();
			return entity;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}