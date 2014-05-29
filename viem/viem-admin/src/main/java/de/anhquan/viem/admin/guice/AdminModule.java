package de.anhquan.viem.admin.guice;

import com.google.inject.AbstractModule;

public class AdminModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new BinderModule());
		install(new DispatchServletModule());
	}

}
