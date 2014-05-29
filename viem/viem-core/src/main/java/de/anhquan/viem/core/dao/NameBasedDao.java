package de.anhquan.viem.core.dao;

import java.util.List;

import de.anhquan.viem.core.model.NameBasedEntity;

public abstract class NameBasedDao<T extends NameBasedEntity> extends BaseDao<T> {

	protected NameBasedDao(final Class<T> clazz) {
        super(clazz);
    }

	public Integer count() {
		return ofy().query(clazz).count();
	}

	public List<T> getItemsHasName(String name){
		return ofy().query(clazz).filter("name", name).list();
	}
	
	public T findFirstItemWithName(String name) {
		List<T> founds = ofy().query(clazz).filter("name", name).list();
		if (founds.isEmpty())
			return null;
		return founds.get(0);
	}
	
	public List<T> getItemsHasNameStartsWith(String prefix) {
		return ofy().query(clazz).filter("name >=", prefix).filter("name <",prefix + "\uFFFD").list();
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
}