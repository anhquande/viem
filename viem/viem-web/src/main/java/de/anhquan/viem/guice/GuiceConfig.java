package de.anhquan.viem.guice;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import de.anhquan.viem.admin.guice.AdminModule;
import de.anhquan.viem.core.guice.CoreModule;
import de.anhquan.viem.frontend.guice.FrontEndModule;

public class GuiceConfig extends GuiceServletContextListener {
	
	public static final Logger log = Logger.getLogger(GuiceConfig.class
			.getName());

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new CoreModule(), new AdminModule(), new FrontEndModule());
	}

}
