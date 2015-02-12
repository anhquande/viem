package de.anhquan.viem.core.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

@Entity
@Cache
public class Page extends NameBasedEntity {

	private static final long serialVersionUID = 1L;
	
	private String title;
	
	private String description;

	private String template;
	
	private String content;
	
	private boolean visible;
	
	public static final Logger log = Logger.getLogger(Page.class.getName());

	public Page(){
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM-HH:mm:sss");
		String now = df.format(new Date());
		this.setTitle("Newpage-" + now);
		this.setName("newpage-"+now);
		this.setTemplate("template");
		this.visible = true;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}