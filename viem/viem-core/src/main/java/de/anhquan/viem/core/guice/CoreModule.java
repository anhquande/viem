package de.anhquan.viem.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.anhquan.viem.core.dao.cache.AppSettingManager;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(AppSettingManager.class).in(Singleton.class);	
	}

}
