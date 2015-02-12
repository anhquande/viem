package de.anhquan.viem.core.model;

import java.util.Collections;
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
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;

import de.anhquan.viem.core.util.Parser;

@Entity
@Cache
public class Option extends BaseEntity implements Comparable<Option>, JSONAble{

	private static final long serialVersionUID = 1L;

	private String title = "default option";
	private String internalComment = "";
	private String type = "default";
	private int displayType = 1;
	@Index
	private int sortValue = 0;
	private boolean visible = true; 
	private String icon;
	
	@Load
	private
	List<Ref<OptionItem>> optionItems;
	
	@Ignore
	@IgnoreLoad
	@IgnoreSave
	private List<OptionItem> importedOptionItems;

	public static final Logger log = Logger.getLogger(Option.class.getName());
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public String getInternalComment() {
		return internalComment;
	}

	public void setInternalComment(String internalComment) {
		this.internalComment = internalComment;
	}
	
	@Override
	public int compareTo(Option obj) {
		if (obj == null)
			return -1;
		
		int diff = this.sortValue - obj.getSortValue();
		if (diff<0)
			return -1;
		if (diff>0)
			return 1;
		
		return 0;
	}
	
	@Override
	public void fromJSON(JSONObject json) {
		if (json == null)
			return;

		super.fromJSON(json);
		
		JSONArray opts = (JSONArray)json.get("optionItems");
		importedOptionItems = new LinkedList<OptionItem>();
		if (opts!=null){
			for(int i=0;i<opts.size();i++){
				JSONObject obj = (JSONObject) opts.get(i);
				OptionItem opt = new OptionItem();
				opt.fromJSON(obj);
				importedOptionItems.add(opt);
			}
		}

	}

	public List<Ref<OptionItem>> getOptionItems() {
		return optionItems;
	}

	public void setOptionItems(List<Ref<OptionItem>> optionItems) {
		this.optionItems = optionItems;
	}

	public List<OptionItem> getOptionItemsAsList(){
		if (this.optionItems == null)
			return new LinkedList<OptionItem>();
		
		List<OptionItem> retList = new LinkedList<OptionItem>();
		
		for(Ref<OptionItem> ref : optionItems){
			OptionItem item = ref.get();
			if (item!=null)
				retList.add(item);
		}
		
		Collections.sort(retList);
		return retList;
	}
	public void addOptionItem(OptionItem item){
		if (item==null)
			return;
		
		if (optionItems==null)
			optionItems = new LinkedList<Ref<OptionItem>>();
		
		optionItems.add(Ref.create(item));
	}
	
	public void removeOptionItem(OptionItem item){
		if ((item==null) || (optionItems==null))
			return;
		
		optionItems.remove(Ref.create(item));
	}
	
	public void removeAllOptionItems(){
		if (optionItems==null)
			optionItems.clear();
	}

	public List<OptionItem> getImportedOptionItems() {
		return importedOptionItems;
	}

	public void setImportedOptionItems(List<OptionItem> importedOptionItems) {
		this.importedOptionItems = importedOptionItems;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}
