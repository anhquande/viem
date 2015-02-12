package de.anhquan.viem.core.guice;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

import com.google.inject.servlet.ServletModule;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.servlet.AdminLoginServlet;
import de.anhquan.viem.core.servlet.AdminLogoutServlet;
import de.anhquan.viem.core.servlet.CategoryAdminServlet;
import de.anhquan.viem.core.servlet.CategoryServlet;
import de.anhquan.viem.core.servlet.ConfigAdminServlet;
import de.anhquan.viem.core.servlet.OptionTypeAdminServlet;
import de.anhquan.viem.core.servlet.PageAdminServlet;
import de.anhquan.viem.core.servlet.PageServlet;
import de.anhquan.viem.core.servlet.ProductAdminServlet;
import de.anhquan.viem.core.servlet.ProductServlet;
import de.anhquan.viem.core.servlet.RobotsServlet;
import de.anhquan.viem.core.servlet.SiteMapServlet;
import de.anhquan.viem.core.servlet.filter.AdminAuthFilter;
import de.anhquan.viem.core.servlet.filter.URLFilter;

public class DispatchServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		registerServlet(PageServlet.class);
		registerServlet(PageAdminServlet.class);
		registerServlet(ProductAdminServlet.class);
		registerServlet(ProductServlet.class);
		registerServlet(CategoryAdminServlet.class);
		registerServlet(CategoryServlet.class);
		registerServlet(OptionTypeAdminServlet.class);
		registerServlet(ConfigAdminServlet.class);
		registerServlet(AdminLoginServlet.class);
		registerServlet(AdminLogoutServlet.class);
		registerServlet(RobotsServlet.class);
		registerServlet(SiteMapServlet.class);

		registerFilter(URLFilter.class);
		registerFilter(AdminAuthFilter.class);
	}
	
	void registerServlet(Class<? extends HttpServlet> cls){		
		serve(cls.getAnnotation(ServletPath.class).value()).with(cls);
	}
	
	void registerFilter(Class<? extends Filter> cls){		
		filter(cls.getAnnotation(ServletPath.class).value()).through(cls);
	}

}
