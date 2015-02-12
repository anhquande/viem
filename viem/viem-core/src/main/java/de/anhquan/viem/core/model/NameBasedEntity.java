package de.anhquan.viem.core.model;

import com.googlecode.objectify.annotation.Index;

@SuppressWarnings("serial")
public abstract class NameBasedEntity extends BaseEntity implements Cloneable, Comparable<NameBasedEntity>, JSONAble{

	@Index
	protected String name;
	
	@Index
	private int sortValue;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
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
		
		if (!(obj instanceof NameBasedEntity))
			return -1;
		
		NameBasedEntity other = (NameBasedEntity)obj;
		
		int diff = this.sortValue - other.getSortValue();
		if (diff<0)
			return -1;
		if (diff>0)
			return 1;
		
		return 0;
	}


}