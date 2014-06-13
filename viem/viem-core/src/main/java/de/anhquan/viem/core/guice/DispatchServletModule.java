package de.anhquan.viem.core.guice;

import javax.servlet.Filter;

import com.google.inject.servlet.ServletModule;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.servlet.AbstractServlet;
import de.anhquan.viem.core.servlet.AdminLoginServlet;
import de.anhquan.viem.core.servlet.AppSettingsServlet;
import de.anhquan.viem.core.servlet.CategoryAdminServlet;
import de.anhquan.viem.core.servlet.CategoryServlet;
import de.anhquan.viem.core.servlet.OptionTypeAdminServlet;
import de.anhquan.viem.core.servlet.PageAdminServlet;
import de.anhquan.viem.core.servlet.PageServlet;
import de.anhquan.viem.core.servlet.ProductAdminServlet;
import de.anhquan.viem.core.servlet.ProductServlet;
import de.anhquan.viem.core.servlet.filter.AdminAuthFilter;

public class DispatchServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		registerServlet(AppSettingsServlet.class);
		registerServlet(PageServlet.class);
		registerServlet(PageAdminServlet.class);
		registerServlet(ProductAdminServlet.class);
		registerServlet(ProductServlet.class);
		registerServlet(CategoryAdminServlet.class);
		registerServlet(CategoryServlet.class);
		registerServlet(OptionTypeAdminServlet.class);
		registerServlet(AdminLoginServlet.class);

		registerFilter(AdminAuthFilter.class);
	}
	
	void registerServlet(Class<? extends AbstractServlet> cls){		
		serve(cls.getAnnotation(ServletPath.class).value()).with(cls);
	}
	
	void registerFilter(Class<? extends Filter> cls){		
		filter(cls.getAnnotation(ServletPath.class).value()).through(cls);
	}

}
