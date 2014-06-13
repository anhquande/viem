package de.anhquan.viem.core.model;

import java.util.logging.Logger;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class OptionItem extends BaseEntity implements Comparable<OptionItem>,
		JSONAble {

	private static final long serialVersionUID = 1L;
	
	private String title = "default option item";
	private String internalComment = "";
	private String image = "";
	private int price = 0;
	private int weight = 0;
	private boolean isDefault = false;
	private boolean visible = true;
	@Index
	private int sortValue;

	public static final Logger log = Logger.getLogger(OptionItem.class
			.getName());

	@Override
	public int compareTo(OptionItem obj) {
		if (obj == null)
			return -1;

		int diff = this.sortValue - obj.getSortValue();
		if (diff < 0)
			return -1;
		if (diff > 0)
			return 1;

		return 0;
	}

	public String getInternalComment() {
		return internalComment;
	}

	public void setInternalComment(String text) {
		this.internalComment = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		System.out.println("newSortValue = "+sortValue);
		this.sortValue = sortValue;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
