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
public class Category extends NameBasedEntity {

	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String shortDescription;

	private String template;
	
	private String fullDescription;
		
	private String thumbnail;
	
	private String image1;
	private String image2;
	private String image3;
	
	private boolean visible;
	
	@Load
	private List<Ref<Category>> subCategories;

	@Ignore
	@IgnoreLoad
	@IgnoreSave
	@IgnoreJson
	private List<Category> importedSubCategories; 
		
	public static final Logger log = Logger.getLogger(Category.class.getName());

	public Category(){
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM-HH:mm:sss");
		String now = df.format(new Date());
		setTitle("New Category - "+now);
		setName("new-category-"+now);
		setTemplate("default");
		setVisible(true);
		
		subCategories= null;
		importedSubCategories = null;
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
		
		JSONArray cats = (JSONArray)json.get("subCategories");
		setImportedSubCategories(new LinkedList<Category>());
		if (cats!=null){
			for(int i=0;i<cats.size();i++){
				JSONObject obj = (JSONObject) cats.get(i);
				Category cat = new Category();
				cat.fromJSON(obj);
				importedSubCategories.add(cat);
			}
		}

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

	public void clearSubCategories(){
		if (subCategories != null)
			subCategories.clear();
	}
	
	public void addSubCategory(Category category){
		if ((category==null) || (category==this))
			return;
		
		if (subCategories==null)
			subCategories = new LinkedList<Ref<Category>>();
		
		subCategories.add(Ref.create(category));
	}

	public List<Category> getSubCategoriesAsList(){
		if (subCategories==null)
			return new LinkedList<Category>();
		
		List<Category> retList = new LinkedList<Category>();
		for (Ref<Category> ref : subCategories) {
			Category cat = ref.get();
			if (cat!=null){
				retList.add(cat);
			}
		}
		
		return retList;
	}
	
	public List<Ref<Category>> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Ref<Category>> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Category> getImportedSubCategories() {
		return importedSubCategories;
	}

	public void setImportedSubCategories(List<Category> importedSubCategories) {
		this.importedSubCategories = importedSubCategories;
	}

	public void clearImportedSubCategories() {
		if (importedSubCategories==null)
			return;
		
		importedSubCategories.clear();
		importedSubCategories = null;
	}
}