package de.anhquan.viem.frontend.guice;

import com.google.inject.servlet.ServletModule;

import de.anhquan.viem.core.servlet.AbstractServlet;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.servlet.PageAdminServlet;
import de.anhquan.viem.frontend.servlet.PageServlet;
import de.anhquan.viem.frontend.servlet.ProductAdminServlet;
import de.anhquan.viem.frontend.servlet.ProductCategoryAdminServlet;
import de.anhquan.viem.frontend.servlet.ProductCategoryServlet;
import de.anhquan.viem.frontend.servlet.ProductServlet;

public class DispatchServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		register(PageServlet.class);
		register(PageAdminServlet.class);
		register(ProductAdminServlet.class);
		register(ProductServlet.class);
		register(ProductCategoryAdminServlet.class);
		register(ProductCategoryServlet.class);
	}
	
	void register(Class<? extends AbstractServlet> cls){		
		serve(cls.getAnnotation(ServletPath.class).basePath()).with(cls);
	}
}
