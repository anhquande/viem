package de.anhquan.viem.core.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public abstract class NameBasedEntity extends BaseEntity implements Cacheable, Cloneable, Comparable<NameBasedEntity>{

	private static final long serialVersionUID = 22198245383626111L;

	public NameBasedEntity() {
    	super();
    }
    
	@Index
	protected String name;
	
	private int sortValue;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	abstract public void copyFrom(Object other);
	
	public String toString(){
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
	
	public String toJSON(){
		return "";
	}
	
	public abstract void fromJSON(JSONObject item);

		@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof NameBasedEntity)) {
			return false;
		}

		NameBasedEntity other = (NameBasedEntity) obj;

		if (this.name == null)
			return false;

		return name.equals(other.name);
	}
	
	@Override
	public int compareTo(NameBasedEntity obj) {
		if (obj == null)
			return -1;
		
		if ((obj.name == null) && (this.name==null))
			return 0;
		
		if (obj.name == null)
			return -1;
		
		if (obj.name.isEmpty() && this.name.isEmpty())
			return 0;
		
		return this.name.compareTo(obj.name);
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}
}