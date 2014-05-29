package de.anhquan.viem.frontend.guice;

import java.util.logging.Logger;

import com.google.inject.AbstractModule;

public class FrontEndModule extends AbstractModule {

	public static final Logger log = Logger.getLogger(FrontEndModule.class
			.getName());
	
	@Override
	protected void configure() {
		install(new BinderModule());
		install(new DispatchServletModule());
	}
}