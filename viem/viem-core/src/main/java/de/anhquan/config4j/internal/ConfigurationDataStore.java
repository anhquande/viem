package de.anhquan.config4j.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.lang.StringUtils;

import de.anhquan.viem.core.dao.ConfigDao;
import de.anhquan.viem.core.model.ConfigEntity;

public class ConfigurationDataStore implements ConfigurationProvider {

	final static ConfigDao configDao = new ConfigDao();
	
	public ConfigurationDataStore(){
	}
	
	@Override
	public void createKey(String key, Object initValue, String title, String description, int sortValue){
		System.out.println("Create key ="+title);
		key = StringUtils.trimToEmpty(key);
		if (key.isEmpty()){
			System.out.println("Cannot create empty key");
			return;
		}
		
		ConfigEntity config = addPropertyDirect(key, initValue);
		if (config!=null){
			config.setTitle(title);
			config.setDescription(description);
			config.setSortValue(sortValue);
			config.setValueType(initValue.getClass().getName());
			configDao.put(config);
		}
	}
	
	@Override
	public boolean containsKey(String key) {
		return configDao.findFirstItemWithName(key) != null;
	}

	@Override
	public Iterator<String> getKeys() {
		List<ConfigEntity> configs = configDao.getAll();
		List<String> keys = new ArrayList<String>();
		for (ConfigEntity configEntity : configs) {
			keys.add(configEntity.getName());
		}
		return keys.iterator();
	}

	@Override
	public Object getProperty(String key) {
		return configDao.findFirstItemWithName(key);
	}

	@Override
	public boolean isEmpty() {
		return configDao.count() == 0;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	protected ConfigEntity addPropertyDirect(String key, Object value) {

		if (value==null)
			return null;
		
		ConfigEntity config = configDao.findFirstItemWithName(key);
		if (config==null){
			config = new ConfigEntity();
		}
		config.setName(key);
		Class<?> valueType = value.getClass();
		if (valueType.equals(String.class)){
			config.setStringValue((String)value);
		}
		else if (valueType.equals(Boolean.class) || valueType.equals(boolean.class)){
			config.setBooleanValue((Boolean)value);
		}
		else if (valueType.equals(Integer.class) || valueType.equals(int.class)){
			config.setIntegerValue((Integer)value);
		}
		else if (valueType.equals(Double.class) || valueType.equals(double.class)){
			config.setDoubleValue((Double)value);
		}
		else if (valueType.equals(Long.class) || valueType.equals(long.class)){
			config.setLongValue((Long)value);
		}
		else if (valueType.equals(Short.class) || valueType.equals(short.class)){
			config.setDoubleValue((Double)value);
		}
		else if (valueType.equals(Byte.class) || valueType.equals(byte.class)){
			config.setByteValue((Byte)value);
		}
		else if (valueType.equals(Float.class) || valueType.equals(float.class)){
			config.setFloatValue((Float)value);
		}
		else if (valueType.equals(List.class)){
			System.out.println(valueType);
		}
		else
			throw new RuntimeException("Cannot store a value of "+valueType.getName()+" into Config Table");
			
		configDao.put(config);
		System.out.println("just create a new key:id="+config.getId()+" - name="+config.getName()+"");
		return config;
	}

	@Override
	public Configuration subset(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addProperty(String key, Object value) {
		addPropertyDirect(key, value);
	}

	@Override
	public void setProperty(String key, Object value) {
		addPropertyDirect(key, value);
	}

	@Override
	public void clearProperty(String key) {
		configDao.delete(key);
	}

	@Override
	public void clear() {
		configDao.clearAll();
	}

	@Override
	public Iterator<String> getKeys(String prefix) {
		
		List<ConfigEntity> entities = configDao.getItemsHasNameStartsWith(prefix);
		List<String> keys = new ArrayList<String>();
		for (ConfigEntity entity: entities) {
			keys.add(entity.getName());
		}
		return keys.iterator();
	}

	@Override
	public Properties getProperties(String key) {
		
		return null;
	}

	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return getBoolean(key,(Boolean)defaultValue).booleanValue();
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Boolean value = (Boolean)entity.getBooleanValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public byte getByte(String key) {
		return getByte(key);
	}

	@Override
	public byte getByte(String key, byte defaultValue) {
		return getByte(key, (Byte)defaultValue).byteValue();
	}

	@Override
	public Byte getByte(String key, Byte defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Byte value = (Byte)entity.getByteValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public double getDouble(String key) {
		return getDouble(key,0);
	}

	@Override
	public double getDouble(String key, double defaultValue) {
		return getDouble(key, (Double)defaultValue).doubleValue();
	}

	@Override
	public Double getDouble(String key, Double defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Double value = (Double)entity.getDoubleValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public float getFloat(String key) {
		return getFloat(key,0);
	}

	@Override
	public float getFloat(String key, float defaultValue) {
		return getFloat(key, (Float)defaultValue).floatValue();
	}

	@Override
	public Float getFloat(String key, Float defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Float value = (Float)entity.getFloatValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public int getInt(String key) {
		return getInt(key,0);
	}

	@Override
	public int getInt(String key, int defaultValue) {
		return getInteger(key, (Integer)defaultValue).intValue();
	}

	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Integer value = (Integer)entity.getIntegerValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public long getLong(String key) {
		return getLong(key,0);
	}

	@Override
	public long getLong(String key, long defaultValue) {
		return getLong(key, (Long)defaultValue).longValue();
	}

	@Override
	public Long getLong(String key, Long defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Long value = (Long)entity.getLongValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public short getShort(String key) {
		return getShort(key, (short)0);
	}

	@Override
	public short getShort(String key, short defaultValue) {
		return getShort(key, (Short)defaultValue).shortValue();
	}

	@Override
	public Short getShort(String key, Short defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			Short value = (Short)entity.getShortValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public BigDecimal getBigDecimal(String key) {
		return getBigDecimal (key, null);
	}

	@Override
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			BigDecimal value = (BigDecimal)entity.getBigDecimalValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public BigInteger getBigInteger(String key) {
		return getBigInteger(key, null);
	}

	@Override
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			BigInteger value = (BigInteger)entity.getBigIntegerValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public String getString(String key) {
		return getString(key, null);
	}

	@Override
	public String getString(String key, String defaultValue) {
		ConfigEntity entity = configDao.findFirstItemWithName(key);
		if (entity != null){
			String value = (String)entity.getStringValue();
			return value;
		}
		return defaultValue;
	}

	@Override
	public String[] getStringArray(String key) {
		List<ConfigEntity> entities = configDao.getItemsHasName(key);
		String[] strList = new String[entities.size()];;
		int i=0;
		for (ConfigEntity configEntity : entities) {
			strList[i++] = (String)configEntity.getStringValue();
		}
		
		return strList;
	}

	@Override
	public List<Object> getList(String key) {
		return getList(key, null);
	}

	@Override
	public List<Object> getList(String key, List<?> defaultValue) {
		List<ConfigEntity> entities = configDao.getItemsHasName(key);
		if (entities.isEmpty())
			return (List<Object>) defaultValue;
		
		List<Object> listObj = new ArrayList<Object>();
		for (ConfigEntity configEntity : entities) {
			listObj.add(configEntity.getStringValue());
		}
		
		return listObj;
	}

}
