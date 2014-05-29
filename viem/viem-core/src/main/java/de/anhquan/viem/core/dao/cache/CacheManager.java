package de.anhquan.viem.core.dao.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.anhquan.viem.core.dao.BaseDao;
import de.anhquan.viem.core.model.NameBasedEntity;

public abstract class CacheManager<T extends NameBasedEntity> {

	protected List<T> cache = new LinkedList<T>();
	
	protected BaseDao<T> dao;
	
	protected boolean isChanged = false;
	
	public static final Logger log = Logger.getLogger(CacheManager.class.getName());

	public void clearCache(){
		cache.clear();
		isChanged = false;
	}
	
	public List<T> getAll(){
		Collections.sort(cache);
		return cache;
	}
	
	public void writeToDataStore() {
		if (isChanged){
			for(T item:cache){
				List<T> entities = dao.listByProperty("name", item.getName());
				if (entities.isEmpty()){
					T e = dao.put(item);	
					item.setId(e.getId()); //save generated ID back to cache
				}else{
					T entity = entities.get(0);
					entity.copyFrom(item);
					dao.put(entity);
				}
			}
			isChanged = false;
		}
	}

	public void readFromDataStore() {
		cache.clear();
		List<T> entities = dao.getAll();
		
		for (T entity : entities) {				
			T newEntity = createObject();
			newEntity.copyFrom(entity);
			cache.add(newEntity);
		}
		
		isChanged = false;
	}

	
	abstract public T createObject();
	
	public T findByName(String name) {
		if ((name == null) || (name.isEmpty()))
			return null;

		for(T entity : cache){
			if (entity.getName().compareTo(name)==0){
				return entity;
			}
		}
		return null;
	}

	public void delete(T entity){
		cache.remove(entity);
	}
	
	public void put(T newEntity){
		if (newEntity==null)
			return;
		
		String entityName = newEntity.getName();
		if ((entityName==null) || (entityName.isEmpty()))
			return;
		
		T obj = null;
		for (T s : cache) {
			if (s.getName().compareTo(entityName)==0)
				obj = s;
		}
		
		if (obj!=null){
			obj.copyFrom(newEntity);
		}
		else{
			cache.add(newEntity);
		}
		
		isChanged = true;
	}

	public void sort(String newItems) {
		if (newItems==null)
			return;
		String[] names = newItems.split(",");
		int sortValue = 0;
		List<T> sortedList = new ArrayList<T>();
		for (String name : names) {
			T item = findByName(name);
			if (item!=null){
				item.setSortValue(sortValue++);
				sortedList.add(item);
			}
		}
		
		for (T entity : cache){
			if (!sortedList.contains(entity)){
				sortedList.add(entity);
				entity.setSortValue(sortValue++);
			}
		}
		Collections.sort(cache);
	}
	
}
