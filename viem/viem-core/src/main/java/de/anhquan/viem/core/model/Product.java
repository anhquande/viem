package de.anhquan.viem.core.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.IgnoreLoad;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Load;

import de.anhquan.viem.core.annotation.IgnoreJson;

@Entity
@Cache
public class Product extends NameBasedEntity {

	private static final long serialVersionUID = 1L;
		
	private String title;
	
	private String shortDescription;

	private String template;
	
	private String fullDescription;
	
	private int basePrice;
	
	private int comparePrice;
	
	private int quantity;
	
	private String thumbnail;
	
	private String image1;
	
	private String image2;
	
	private String image3;
	
	private boolean visible;
	
	private int defaultSpicyLevel;
	
	private boolean vegetarian;
	
	@Load
	private List<Ref<Option>> options;
	
	@Ignore
	@IgnoreLoad
	@IgnoreSave
	private List<Category> categories;
	
	@Ignore
	@IgnoreLoad
	@IgnoreSave
	@IgnoreJson
	private List<Category> importedCategories;
	
	@Ignore
	@IgnoreLoad
	@IgnoreSave
	@IgnoreJson
	private List<Option> importedOptions;
		
	public static final Logger log = Logger.getLogger(Product.class.getName());

	public Product(){
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM-HH:mm:sss");
		String now = df.format(new Date());
		name = "new-product-"+now;
		title = "new-product-"+now;
		visible = true;
		template ="default";
		defaultSpicyLevel = 0;
		vegetarian = false;
		categories = new LinkedList<Category>();
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

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String content) {
		this.fullDescription = content;
	}
	
	@Override
	public void fromJSON(JSONObject json) {
		if (json == null)
			return;
		
		super.fromJSON(json);

		JSONArray opts = (JSONArray)json.get("options");
		importedOptions = new LinkedList<Option>();
		if (opts!=null){
			for(int i=0;i<opts.size();i++){
				JSONObject obj = (JSONObject) opts.get(i);
				Option opt = new Option();
				opt.fromJSON(obj);
				importedOptions.add(opt);
			}
		}
		
		JSONArray cats = (JSONArray)json.get("categories");
		importedCategories = new LinkedList<Category>();
		if (cats!=null){
			for(int i=0;i<cats.size();i++){
				JSONObject obj = (JSONObject) cats.get(i);
				Category cat = new Category();
				cat.fromJSON(obj);
				importedCategories.add(cat);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject toJSONObject() {
		JSONObject json = super.toJSONObject();

		JSONArray listOptions = new JSONArray();
		if (options!=null){
			for (Ref<Option> ref : options) {
				listOptions.add(ref.get().toJSONObject());
			}
		}
		json.put("options",listOptions);
		
		JSONArray listCategories = new JSONArray();
		if (categories !=null){
			for(Category cat : categories) {
				listCategories.add(cat.toJSONObject());
			}
		}
		json.put("categories",listCategories);

		return json;
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
	
	public List<Ref<Option>> getOptions() {
		if (options==null)
			return new LinkedList<Ref<Option>>();
		return options;
	}

	public List<Option> getOptionsAsList() {
		if (options == null)
			return new LinkedList<Option>();
		
		List<Option> retList = new LinkedList<Option>();
		for(Ref<Option> ref : options){
			Option option = ref.get();
			if (option!=null)
				retList.add(option);
		}
		
		return retList;
	}
	
	public void setOptions(List<Ref<Option>> options) {
		this.options = options;
	}

	public void addOption(Option option) {
		if (option == null)
			return;
		if (options == null)
			options = new LinkedList<Ref<Option>>();
		
		options.add(Ref.create(option));
	}

	public void removeOption(Option option) {
		if (option == null)
			return;
		if (options != null){
			options.remove(Ref.create(option));
		}
	}
	
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category target) {
		if (categories==null)
			categories = new LinkedList<Category>();
		
		if (!categories.contains(target))
			categories.add(target);
	}

	public void clearCategories() {
		if (categories==null)
			categories = new LinkedList<Category>();
		
		categories.clear();
	}

	public List<Option> getImportedOptions() {
		return importedOptions;
	}

	public void setImportedOptions(List<Option> importedOptions) {
		this.importedOptions = importedOptions;
	}

	public List<Category> getImportedCategories() {
		return importedCategories;
	}

	public void setImportedCategories(List<Category> importedCategories) {
		this.importedCategories = importedCategories;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getDefaultSpicyLevel() {
		return defaultSpicyLevel;
	}

	public void setDefaultSpicyLevel(int defaultSpicyLevel) {
		this.defaultSpicyLevel = defaultSpicyLevel;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

}