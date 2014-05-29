package de.anhquan.viem.frontend.model;

import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Entity;

import de.anhquan.viem.core.model.Cacheable;
import de.anhquan.viem.core.model.NameBasedEntity;

@Entity
public class ProductCategory extends NameBasedEntity implements Cacheable{

	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String shortDescription;

	private String template;
	
	private String fullDescription;
		
	private int sortValue;
	
	private String thumbnail;
	
	private String image1;
	private String image2;
	private String image3;
	
	private boolean visible = true;
		
	public static final Logger log = Logger.getLogger(ProductCategory.class.getName());

	public ProductCategory(){
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String description) {
		this.shortDescription = description;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public void copyFrom(Object other) {
		if (other == null)
			return;
		
		ProductCategory entity;
		try{
			entity = (ProductCategory)other;
			if (entity.getId()!=null)
				this.setId(entity.getId());
			
			this.name = entity.getName();
			this.title = entity.getTitle();
			this.fullDescription = entity.getFullDescription();
			this.shortDescription = entity.getShortDescription();
			this.template = entity.getTemplate();
			this.sortValue= entity.getSortValue();
			this.visible= entity.isVisible();
			this.thumbnail = entity.getThumbnail();
			this.image1 = entity.getImage1();
			this.image2 = entity.getImage2();
			this.image3 = entity.getImage3();
		}
		catch(Exception e){
			log.info("Exception when copyFrom other Page entity."+e);
		}
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String content) {
		this.fullDescription = content;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON(){
		
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("title", this.title);
		json.put("fullDescription", this.fullDescription);
		json.put("shortDescription", this.shortDescription);
		json.put("template", this.template);
		json.put("sortValue", this.sortValue);
		json.put("thumbnail", this.thumbnail);
		json.put("image1", this.image1);
		json.put("image2", this.image2);
		json.put("image3", this.image3);
		json.put("visible", this.visible);

		return json.toJSONString();
	}


	@Override
	public void fromJSON(JSONObject json) {
		
		if (json == null)
			return;
		log.info(json.toJSONString());
		this.name = (String) json.get("name");
		this.title = (String) json.get("title");
		this.fullDescription = (String) json.get("fullDescription");
		this.shortDescription = (String) json.get("shortDescription");
		this.template = (String) json.get("template");
		this.sortValue = ((Long)json.get("sortValue")).intValue();;
		this.thumbnail = (String) json.get("thumbnail");
		this.image1 = (String) json.get("image1");
		this.image2 = (String) json.get("image2");
		this.image3 = (String) json.get("image3");
		this.visible = (Boolean) json.get("visible");

	}
	
	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getImage1() {
		return image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	public String getImage2() {
		return image2;
	}

	public void setImage2(String image2) {
		this.image2 = image2;
	}

	public String getImage3() {
		return image3;
	}

	public void setImage3(String image3) {
		this.image3 = image3;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public int compareTo(NameBasedEntity obj) {
		if (obj == null)
			return -1;
		
		if (!(obj instanceof ProductCategory))
			return -1;
		
		ProductCategory other = (ProductCategory)obj;
		
		int diff = this.sortValue - other.getSortValue();
		if (diff<0)
			return -1;
		if (diff>0)
			return 1;
		
		return 0;

	}
}
