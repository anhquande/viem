package de.anhquan.config4j;

import java.util.List;

public interface Config extends Cloneable {

	public List<String> getKeys();
	
	public List<ConfigItem> getConfigItems();
	
	public ConfigItem getConfigItem(String key);
	
	public void resetAll();
}