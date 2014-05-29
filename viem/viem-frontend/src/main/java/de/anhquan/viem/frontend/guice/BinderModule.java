package de.anhquan.viem.frontend.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.anhquan.viem.frontend.dao.cache.PageManager;
import de.anhquan.viem.frontend.dao.cache.ProductCategoryManager;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.servlet.PageAdminServlet;
import de.anhquan.viem.frontend.servlet.PageServlet;
import de.anhquan.viem.frontend.servlet.ProductAdminServlet;
import de.anhquan.viem.frontend.servlet.ProductCategoryAdminServlet;
import de.anhquan.viem.frontend.servlet.ProductCategoryServlet;
import de.anhquan.viem.frontend.servlet.ProductServlet;

public class BinderModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(PageManager.class).in(Singleton.class);
		bind(ProductManager.class).in(Singleton.class);
		bind(ProductCategoryManager.class).in(Singleton.class);
		bind(PageServlet.class).in(Singleton.class);
		bind(PageAdminServlet.class).in(Singleton.class);
		bind(ProductAdminServlet.class).in(Singleton.class);
		bind(ProductServlet.class).in(Singleton.class);
		bind(ProductCategoryAdminServlet.class).in(Singleton.class);
		bind(ProductCategoryServlet.class).in(Singleton.class);
	}
}