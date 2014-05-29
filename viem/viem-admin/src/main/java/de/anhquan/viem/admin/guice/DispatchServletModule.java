package de.anhquan.viem.admin.guice;

import com.google.inject.servlet.ServletModule;

import de.anhquan.viem.core.servlet.AppSettingsServlet;

public class DispatchServletModule extends ServletModule {

	@Override
	protected void configureServlets() {

		serve("/admin/setting").with(AppSettingsServlet.class);

	}

}
