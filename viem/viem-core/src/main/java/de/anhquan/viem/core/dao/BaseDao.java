/**
 * Copyright 2012 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.anhquan.viem.core.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.Embedded;
import javax.persistence.Transient;

import com.google.common.collect.Lists;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import de.anhquan.viem.core.dao.objectify.Ofy;
import de.anhquan.viem.core.dao.objectify.OfyFactory;

public abstract class BaseDao<T> {
    protected final Class<T> clazz;

    @Inject
    OfyFactory ofyFactory;

    private Ofy lazyOfy;

    protected BaseDao(final Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> getAll() {
        return ofy().query(clazz).list();
    }

    public T put(T object) {
        ofy().save().entity(object).now();

        return object;
    }

    public Collection<T> put(Iterable<T> entities) {
        return ofy().save().entities(entities).now().values();
    }

    public T get(Key<T> key) {
        return ofy().get(key);
    }

    public T get(Long id) {
        // work around for objectify cacheing and new query not having the
        // latest
        // data
        ofy().clear();

        return ofy().get(clazz, id);
    }

    public Boolean exists(Key<T> key) {
        return get(key) != null;
    }

    public Boolean exists(Long id) {
        return get(id) != null;
    }

    public List<T> getSubset(List<Long> ids) {
        return new ArrayList<T>(ofy().query(clazz).ids(ids).values());
    }

    public Map<Long, T> getSubsetMap(List<Long> ids) {
        return new HashMap<Long, T>(ofy().query(clazz).ids(ids));
    }

    public void delete(T object) {
        ofy().delete().entity(object);
    }

    public void delete(Long id) {
        Key<T> key = Key.create(clazz, id);
        ofy().delete().entity(key);
    }

    public void delete(List<T> objects) {
        ofy().delete().entities(objects);
    }

    public List<T> get(List<Key<T>> keys) {
        return Lists.newArrayList(ofy().load().keys(keys).values());
    }
    
    protected Ofy ofy() {
        if (lazyOfy == null) {
            lazyOfy = ofyFactory.begin();
        }
        return lazyOfy;
    }

    public LoadType<T> query() {
        return ofy().query(clazz);
    }
    
    public List<T> listByProperty(String propName, Object propValue)
    {
        Query<T> q = ofy().query(clazz);
        return q.filter(propName, propValue).list();        
    }
    
    public List<T> listByProperty(String propName, Object propValue, int offset, int limit, String orderBy)
    {
        Query<T> q = ofy().query(clazz).filter(propName, propValue);
        return addLimitOrderOptions(q, offset, limit, orderBy).list();
    }
    
    public List<T> listFrom(int offset, int limit, String orderBy)
    {
        Query<T> q = ofy().query(clazz);
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
        Query<T> q = ofy().query(clazz);
 
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
		return ofy().query(clazz).count();
	}
 
}
