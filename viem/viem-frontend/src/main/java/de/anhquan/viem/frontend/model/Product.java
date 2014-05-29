package de.anhquan.viem.frontend.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;

import de.anhquan.viem.core.model.Cacheable;
import de.anhquan.viem.core.model.NameBasedEntity;

@Entity
public class Product extends NameBasedEntity implements Cacheable{

	private static final long serialVersionUID = 1L;
		
	//SKU = NAME
	
	private String title;
	
	private String shortDescription;

	private String template;
	
	private String fullDescription;
	
	private int basePrice;
	
	private int comparePrice;
	
	private int quantity;
	
	private String productCategories;
	
	private String thumbnail;
	
	private String image1;
	
	private String image2;
	
	private String image3;
	
	@Ignore
	private List<String> listProductCategories;
	
	private int sortValue;
		
	public static final Logger log = Logger.getLogger(Product.class.getName());

	public Product(){
		listProductCategories = new LinkedList<String>();
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
		log.info("copyFrom "+other.toString());

		Product entity;
		try{
			entity = (Product)other;
			if (entity.getId()!=null)
				this.setId(entity.getId());
			
			this.name = entity.getName();
			this.title = entity.getTitle();
			this.fullDescription = entity.getFullDescription();
			this.shortDescription = entity.getShortDescription();
			this.template = entity.getTemplate();
			this.basePrice = entity.getBasePrice();
			this.comparePrice = entity.getComparePrice();
			this.parseProductCategories(entity.getProductCategories());
			this.sortValue = entity.getSortValue();
			this.quantity = entity.getQuantity();
			this.thumbnail = entity.getThumbnail();
			this.image1 = entity.getImage1();
			this.image2 = entity.getImage2();
			this.image3 = entity.getImage3();
		}
		catch(Exception e){
			log.info("Exception when copyFrom other Product entity."+e);
		}
	}

	public String getProductCategories() {
		return productCategories;
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
		json.put("basePrice", this.basePrice);
		json.put("comparePrice", this.comparePrice);
		json.put("sortValue",this.sortValue);
		json.put("quantity",this.quantity);
		json.put("thumbnail",this.thumbnail);
		json.put("image1",this.image1);
		json.put("image2",this.image2);
		json.put("image3",this.image3);

		json.put("productCategories",this.productCategories);
		
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
		
		this.basePrice = ((Long)json.get("basePrice")).intValue();
		this.comparePrice = ((Long)json.get("comparePrice")).intValue();
		this.sortValue = ((Long)json.get("sortValue")).intValue();
		this.quantity = ((Long)json.get("quantity")).intValue();
		this.thumbnail = (String) json.get("thumbnail");
		this.image1 = (String) json.get("image1");
		this.image2 = (String) json.get("image2");
		this.image3 = (String) json.get("image3");
		parseProductCategories((String)json.get("productCategories"));
		
	}
	public int getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(int basePrice) {
		this.basePrice = basePrice;
	}

	public int getComparePrice() {
		return comparePrice;
	}

	public void setComparePrice(int comparePrice) {
		this.comparePrice = comparePrice;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}
	
	public void addProductCategory(String category){
		listProductCategories.add(category);
		productCategories = listToString(listProductCategories);
	}
	
	public void removeProductCategory(String category){
		listProductCategories.remove(category);
		productCategories = listToString(listProductCategories);
	}

	public void clearAllProductCategories(){
		listProductCategories.clear();
		productCategories="";
	}
	
	public List<String> getProductCategoriesAsList(){
		return listProductCategories;
	}
	
	public void parseProductCategories(String str){
		clearAllProductCategories();
		
		if ((str==null) || str.isEmpty())
			return;

		listProductCategories = new LinkedList<String>(Arrays.asList(str.split(";")));
		productCategories = listToString(listProductCategories);
	}

	private String listToString(List<String> list){
		int len = list.size();
		StringBuilder builder = new StringBuilder();
		int theLastIndex = len-1;
		for(int i=0;i<len;i++){
			builder.append(list.get(i));
			if (i<theLastIndex){
				builder.append(";");
			}
		}
		
		return builder.toString();
	}
	
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
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

	public boolean belongsTo(String category) {
		if ((category==null) || (category.isEmpty()))
			return false;

		for(String cat: listProductCategories){
			if (cat.compareToIgnoreCase(category)==0){
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int compareTo(NameBasedEntity obj) {
		if (obj == null)
			return -1;
		
		if (!(obj instanceof Product))
			return -1;
		
		Product other = (Product)obj;
		
		int diff = this.sortValue - other.getSortValue();
		if (diff<0)
			return -1;
		if (diff>0)
			return 1;
		
		return 0;

	}

}