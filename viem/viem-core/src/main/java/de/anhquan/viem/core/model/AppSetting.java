package de.anhquan.viem.core.model;

import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

@Entity
public class AppSetting extends NameBasedEntity implements Cacheable {

	private static final long serialVersionUID = 1L;
	public static final String GROUP_BASIC = "Basic";
	public static final String GROUP_TEXT = "Text";
	public static final String GROUP_STORE = "Store";
	public static final String GROUP_APPLICATION = "Application";
	public static final String STORE_FAX = "store.fax";
	public static final String STORE_TELEPHONE = "store.telephone";
	public static final String STORE_ADDRESS_LINE1 = "store.address_line_1";
	public static final String STORE_ADDRESS_LINE2 = "store.address_line_2";
	public static final String APP_FOOTER2 = "app.footer2";
	public static final String APP_FOOTER1 = "app.footer1";
	public static final String APP_SIDEBAR = "app.sidebar";
	public static final String APP_TOPNAV = "app.topnav";
	public static final String APP_PAGE_META = "app.pagemeta";
	public static final String APP_LOGO = "app.logo";

	public static final String APP_VERSION = "app.version";
	public static final String APP_TITLE = "app.title";
	public static final String APP_THEME = "app.theme";
	public static final String STORE_TIMEZONE = "store.timezone";
	public static final String STORE_LOCALE = "store.locale";
	public static final String STORE_NOW = "store.now";

	public static final String STORE_WEBSITE = "store.website";
	public static final String STORE_EMAIL = "store.email";
	public static final String STORE_NAME = "store.name";
	public static final String STORE_OPENING_TIME = "store.opening_time";
	public static final String TEXT_LATEST_STATUS = "text.latest_status";
	public static final String TEXT_STATUS_TITLE_0_0 = "text.status_title_0_0";
	public static final String TEXT_STATUS_TITLE_0_1 = "text.status_title_0_1";

	public static final String TEXT_STATUS_TITLE_1_0 = "text.status_title_1_0";
	public static final String TEXT_STATUS_TITLE_1_1 = "text.status_title_1_1";

	public static final String TEXT_STATUS_TITLE_2_0 = "text.status_title_2_0";
	public static final String TEXT_STATUS_TITLE_2_1 = "text.status_title_2_1";

	public static final String TEXT_STATUS_TITLE_3_0 = "text.status_title_3_0";
	public static final String TEXT_STATUS_TITLE_3_1 = "text.status_title_3_1";

	public static final String TEXT_STATUS_DESCRIPTION_0_0 = "text.status_description_0_0";
	public static final String TEXT_STATUS_DESCRIPTION_0_1 = "text.status_description_0_1";
	public static final String TEXT_STATUS_DESCRIPTION_1_0 = "text.status_description_1_0";
	public static final String TEXT_STATUS_DESCRIPTION_1_1 = "text.status_description_1_1";
	public static final String TEXT_STATUS_DESCRIPTION_2_0 = "text.status_description_2_0";
	public static final String TEXT_STATUS_DESCRIPTION_2_1 = "text.status_description_2_1";
	public static final String TEXT_STATUS_DESCRIPTION_3_0 = "text.status_description_3_0";
	public static final String TEXT_STATUS_DESCRIPTION_3_1 = "text.status_description_3_1";
	public static final String REFRESH_INTERVAL = "basic.refresh_interval";
	public static final String REFRESH_CONTROL_INTERVAL = "basic.refresh_control_interval";

	public static final String STORE_MODE = "store.mode.busy";

	public static final String STORE_MODE_TITLE_0 = "store.mode.title0";
	public static final String STORE_MODE_DESCRIPTION_0 = "store.mode.description0";
	public static final String STORE_MODE_TITLE_1 = "store.mode.title1";
	public static final String STORE_MODE_DESCRIPTION_1 = "store.mode.description1";
	public static final String STORE_MODE_TITLE_2 = "store.mode.title2";
	public static final String STORE_MODE_DESCRIPTION_2 = "store.mode.description2";
	public static final String STORE_MODE_TITLE_3 = "store.mode.title3";
	public static final String STORE_MODE_DESCRIPTION_3 = "store.mode.description3";

	@Index
	private String title;

	private String value;

	private String defaultValue;

	private int sortValue;

	@Index
	private String group;

	public static final Logger log = Logger.getLogger(AppSetting.class
			.getName());

	public AppSetting() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getSortValue() {
		return sortValue;
	}

	public void setSortValue(int sortValue) {
		this.sortValue = sortValue;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	@Override
	public void copyFrom(Object other) {
		log.info("try to copy values from other object.");
		if (other == null)
			return;

		AppSetting entity;
		try {
			entity = (AppSetting) other;
			if (entity.getId() != null)
				this.setId(entity.getId());

			this.name = entity.getName();
			this.title = entity.getTitle();
			this.value = entity.getValue();
			this.defaultValue = entity.getDefaultValue();
			this.sortValue = entity.getSortValue();
			this.group = entity.getGroup();
		} catch (Exception e) {
			log.info("Exception when copyFrom other AppSetting entity." + e);
		}
	}

	@SuppressWarnings("unchecked")
	public String toJSON() {
		JSONObject json = new JSONObject();
		json.put("name", this.name);
		json.put("title", this.title);
		json.put("value", this.value);
		json.put("defaultValue", this.defaultValue);
		json.put("group", this.group);
		json.put("sortValue", this.sortValue);
		return json.toJSONString();
	}

	public void fromJSON(JSONObject json) {
		if (json == null)
			return;
		this.name = (String) json.get("name");
		this.title = (String) json.get("title");
		this.value = (String) json.get("value");
		this.defaultValue = (String) json.get("defaultValue");
		this.group = (String) json.get("group");
		try {
			Object o = json.get("sortValue");
			this.sortValue = Integer.parseInt(o.toString());
		} finally {
			this.sortValue = 0;
		}

	}
	
	@Override
	public int compareTo(NameBasedEntity obj) {
		if (obj == null)
			return -1;
		if (!(obj instanceof AppSetting))
			return -1;
		
		AppSetting other = (AppSetting)obj;
		int groupCompareValue = this.group.compareTo(other.getGroup());
		if (groupCompareValue == 0) {
			int diff = this.getSortValue() - other.getSortValue();
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else
				return 0;
		}

		return groupCompareValue;
	}
	public int compareTo(AppSetting other) {
		int groupCompareValue = this.group.compareTo(other.getGroup());
		if (groupCompareValue == 0) {
			int diff = this.getSortValue() - other.getSortValue();
			if (diff < 0)
				return -1;
			else if (diff > 0)
				return 1;
			else
				return 0;
		}

		return groupCompareValue;
	}


}
