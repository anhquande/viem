package de.anhquan.viem.core.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embedded;
import javax.persistence.Transient;

import org.json.simple.JSONArray;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import de.anhquan.viem.core.model.JSONAble;

public abstract class BaseDao<T extends JSONAble> {
    protected final Class<T> clazz;

    protected BaseDao(final Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Clear all entities synchronously
     */
    public void clearAll() {
    	delete(getAll());
    }
    
    public List<T> getAll() {
        return ofy().load().type(clazz).list();
    }
    
    public List<T> getAll(String orderBy) {
        return ofy().load().type(clazz).order(orderBy).list();
    }


    public T put(T object) {
        ofy().save().entity(object).now();

        return object;
    }

    public Collection<T> put(Iterable<T> entities) {
        return ofy().save().entities(entities).now().values();
    }
    
    public void clearCache(){
    	ofy().clear();
    }

    public T get(Long id) {
        // work around for objectify cacheing and new query not having the
        // latest
        // data
        ofy().clear();

        return ofy().load().type(clazz).id(id).now();
    }

    public Boolean exists(Long id) {
        return get(id) != null;
    }

    public List<T> getSubset(List<Long> ids) {
        return new ArrayList<T>(ofy().load().type(clazz).ids(ids).values());
    }

    public Map<Long, T> getSubsetMap(List<Long> ids) {
        return new HashMap<Long, T>(ofy().load().type(clazz).ids(ids));
    }

    public void delete(T object) {
        ofy().delete().entity(object).now();
    }

    public void delete(Long id) {
        Key<T> key = Key.create(clazz, id);
        ofy().delete().entity(key).now();
    }

    public void delete(List<T> objects) {
        ofy().delete().entities(objects).now();
    }

    public List<T> get(List<Key<T>> keys) {
        return Lists.newArrayList(ofy().load().keys(keys).values());
    }
    
    public LoadType<T> query() {
        return ofy().load().type(clazz);
    }
        
    public List<T> listByProperty(String propName, Object propValue)
    {
        Query<T> q = ofy().load().type(clazz);
        return q.filter(propName, propValue).list();        
    }
    
    public List<T> listByProperty(String propName, Object propValue, int offset, int limit, String orderBy)
    {
        Query<T> q = ofy().load().type(clazz).filter(propName, propValue);
        return addLimitOrderOptions(q, offset, limit, orderBy).list();
    }
    
    public List<T> listFrom(int offset, int limit, String orderBy)
    {
        Query<T> q = ofy().load().type(clazz);
        return addLimitOrderOptions(q, offset, limit, orderBy).list();
    }
    
    static final int BAD_MODIFIERS = Modifier.FINAL | Modifier.STATIC | Modifier.TRANSIENT;

    public List<T> listByExample(T exampleObj)
    {
        Query<T> queryByExample = buildQueryByExample(exampleObj);
        return queryByExample.list();
    }

    public List<T> listByExample(T exampleObj,int offset, int limit, String orderBy)
    {
        Query<T> queryByExample = buildQueryByExample(exampleObj);
        return queryByExample.limit(limit).offset(offset).order(orderBy).list();
    }
    
    private Query<T> addLimitOrderOptions(Query<T> query,int offset, int limit, String orderBy ) {
    	if ((orderBy == null) || (orderBy.length() == 0))
    		return query.offset(offset).limit(limit);
    	else
    		return query.offset(offset).limit(limit).order(orderBy);
    }
    
    private Query<T> buildQueryByExample(T exampleObj)
    {
        Query<T> q = ofy().load().type(clazz);
 
        // Add all non-null properties to query filter
        for (Field field : clazz.getDeclaredFields())
        {
            // Ignore transient, embedded, array, and collection properties
            if (field.isAnnotationPresent(Transient.class)
                || (field.isAnnotationPresent(Embedded.class))
                || (field.getType().isArray())
                || (Collection.class.isAssignableFrom(field.getType()))
                || ((field.getModifiers() & BAD_MODIFIERS) != 0))
                continue;
 
            field.setAccessible(true);
 
            Object value;
            try
            {
                value = field.get(exampleObj);
            }
            catch (IllegalArgumentException e)
            {
                throw new RuntimeException(e);
            }
            catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            if (value != null)
            {
                q.filter(field.getName(), value);
            }
        }
 
        return q;
    }
    
	public Integer count() {
		return ofy().load().type(clazz).count();
	}
 
	public String toJSONString(){
		return toJSONArray().toJSONString();
	}
	
	public T newObject() {
		try {
			T t =  clazz.newInstance();
			return t;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray toJSONArray(){
		JSONArray json = new JSONArray();
		List<T> entities = getAll();
		for (T t : entities) {
			json.add(t.toJSONObject());
		}
		return json;
	}
	
}
