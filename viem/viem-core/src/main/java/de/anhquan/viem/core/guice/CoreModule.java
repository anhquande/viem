


package de.anhquan.viem.core.guice;

import com.google.inject.AbstractModule;

public class CoreModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new BinderModule());
		install(new DispatchServletModule());
	}

}
