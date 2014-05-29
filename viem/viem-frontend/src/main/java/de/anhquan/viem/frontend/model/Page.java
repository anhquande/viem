package de.anhquan.viem.frontend.model;

import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Entity;

import de.anhquan.viem.core.model.Cacheable;
import de.anhquan.viem.core.model.NameBasedEntity;

@Entity
public class Page extends NameBasedEntity implements Cacheable {

	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String description;

	private String template;
	
	private String content;
	
	public static final Logger log = Logger.getLogger(Page.class.getName());

	public Page(){
	}

	public String getTitle() {
		return title;
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
		
		Page entity;
		try{
			entity = (Page)other;
			if (entity.getId()!=null)
				this.setId(entity.getId());
			
			this.name = entity.getName();
			this.title = entity.getTitle();
			this.content = entity.getContent();
			this.description = entity.getDescription();
			this.template = entity.getTemplate();
		}
		catch(Exception e){
			log.info("Exception when copyFrom other Page entity."+e);
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@SuppressWarnings("unchecked")
	public String toJSON(){
		
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("title", this.title);
		json.put("content", this.content);
		json.put("description", this.description);
		json.put("template", this.template);
		return json.toJSONString();
	}
	
	public void fromJSON(JSONObject json){
		if (json == null)
			return;
		this.name = (String) json.get("name");
		this.title = (String) json.get("title");
		this.content = (String) json.get("content");
		this.description = (String) json.get("description");
		this.template = (String) json.get("template");
	}

}