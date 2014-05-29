package de.anhquan.viem.admin.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.anhquan.viem.core.servlet.AppSettingsServlet;

public class BinderModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(AppSettingsServlet.class).in(Singleton.class);
	}

}
