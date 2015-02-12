package de.anhquan.config4j.internal;

import org.apache.commons.configuration.Configuration;

public interface ConfigurationProvider extends Configuration{

	public void createKey(String key, Object initValue, String title, String description, int sortValue);
}
