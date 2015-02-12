package de.anhquan.viem.core.guice;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

import de.anhquan.viem.core.guice.CoreModule;

public class GuiceConfig extends GuiceServletContextListener {
	
	public static final Logger log = Logger.getLogger(GuiceConfig.class
			.getName());

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new CoreModule());
	}

}
