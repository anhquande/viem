package de.anhquan.viem.core.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

@Entity
@Cache
public class ConfigEntity extends NameBasedEntity{

	private static final long serialVersionUID = -6728061094433388514L;
	
	private String title;
	
	private String description;
	
	private String stringValue;
	
	private String valueType;
	
	private Boolean booleanValue;
	
	private Integer integerValue;
	
	private Float floatValue;
	
	private Double doubleValue;
	
	private Long longValue;
	
	private Short shortValue;
	
	private Byte byteValue;
	
	private BigInteger bigIntegerValue;
	
	private BigDecimal bigDecimalValue;
	
	private List<String> listString;
	
	private List<Boolean> listBoolean;
	
	private List<Integer> listInteger;
	
	private List<Float> listFloat;
	
	private List<Double> listDouble;
	
	public Object getValue(){
		
		try {
			Class<?> cls = Class.forName(valueType);
			if (cls.equals(String.class)) 
				return stringValue;
			else if (cls.equals(Integer.class)) 
				return integerValue;
			else if (cls.equals(Double.class)) 
				return doubleValue;
			else if (cls.equals(Float.class)) 
				return floatValue;
			else if (cls.equals(Short.class)) 
				return shortValue;
			else if (cls.equals(Long.class)) 
				return longValue;
			else if (cls.equals(Byte.class)) 
				return byteValue;
			else if (cls.equals(Boolean.class)) 
				return booleanValue;

			return null;
		} catch (ClassNotFoundException | NullPointerException e) {
			return null;
		}

	}
	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setBooleanValue(Boolean booleanValue) {
		this.booleanValue = booleanValue;
	}

	public Float getFloatValue() {
		return floatValue;
	}

	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}

	public Double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public List<String> getListString() {
		return listString;
	}

	public void setListString(List<String> listString) {
		this.listString = listString;
	}

	public List<Boolean> getListBoolean() {
		return listBoolean;
	}

	public void setListBoolean(List<Boolean> listBoolean) {
		this.listBoolean = listBoolean;
	}

	public List<Integer> getListInteger() {
		return listInteger;
	}

	public void setListInteger(List<Integer> listInteger) {
		this.listInteger = listInteger;
	}

	public List<Float> getListFloat() {
		return listFloat;
	}

	public void setListFloat(List<Float> listFloat) {
		this.listFloat = listFloat;
	}

	public List<Double> getListDouble() {
		return listDouble;
	}

	public void setListDouble(List<Double> listDouble) {
		this.listDouble = listDouble;
	}

	public Long getLongValue() {
		return longValue;
	}

	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}

	public Short getShortValue() {
		return shortValue;
	}

	public void setShortValue(Short shortValue) {
		this.shortValue = shortValue;
	}

	public Byte getByteValue() {
		return byteValue;
	}

	public void setByteValue(Byte byteValue) {
		this.byteValue = byteValue;
	}

	public BigInteger getBigIntegerValue() {
		return bigIntegerValue;
	}

	public void setBigIntegerValue(BigInteger bigIntegerValue) {
		this.bigIntegerValue = bigIntegerValue;
	}

	public BigDecimal getBigDecimalValue() {
		return bigDecimalValue;
	}

	public void setBigDecimalValue(BigDecimal bigDecimalValue) {
		this.bigDecimalValue = bigDecimalValue;
	}

	public String getTitle() {
		return title;
	}

	public Integer getIntegerValue() {
		return integerValue;
	}

	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	
}
