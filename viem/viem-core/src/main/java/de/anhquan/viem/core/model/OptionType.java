package de.anhquan.viem.core.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;

@Cache
@Entity
public class OptionType extends NameBasedEntity{

	private static final long serialVersionUID = 8671282363880503424L;

	private String title;
	
	public OptionType(){
		super();
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM-HH:mm:sss");
		String now = df.format(new Date());
		name = "new-optiontype-"+now;
		title = "new-optiontype-"+now;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
