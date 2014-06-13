package de.anhquan.viem.core.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.DefaultProductDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionItemDao;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.dao.PageDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.servlet.AdminLoginServlet;
import de.anhquan.viem.core.servlet.AppSettingsServlet;
import de.anhquan.viem.core.servlet.CategoryAdminServlet;
import de.anhquan.viem.core.servlet.CategoryServlet;
import de.anhquan.viem.core.servlet.DefaultProductAdminServlet;
import de.anhquan.viem.core.servlet.OptionTypeAdminServlet;
import de.anhquan.viem.core.servlet.PageAdminServlet;
import de.anhquan.viem.core.servlet.PageServlet;
import de.anhquan.viem.core.servlet.ProductAdminServlet;
import de.anhquan.viem.core.servlet.ProductServlet;
import de.anhquan.viem.core.servlet.filter.AdminAuthFilter;

public class BinderModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(AppSettingDao.class).in(Singleton.class);
		bind(PageDao.class).in(Singleton.class);
		bind(ProductDao.class).in(Singleton.class);
		bind(CategoryDao.class).in(Singleton.class);
		bind(DefaultProductDao.class).in(Singleton.class);
		bind(OptionDao.class).in(Singleton.class);
		bind(OptionTypeDao.class).in(Singleton.class);
		bind(OptionItemDao.class).in(Singleton.class);
		bind(ProductCategoryRelationDao.class).in(Singleton.class);

		bind(PageServlet.class).in(Singleton.class);
		bind(PageAdminServlet.class).in(Singleton.class);
		bind(ProductAdminServlet.class).in(Singleton.class);
		bind(DefaultProductAdminServlet.class).in(Singleton.class);
		bind(ProductServlet.class).in(Singleton.class);
		bind(CategoryAdminServlet.class).in(Singleton.class);
		bind(CategoryServlet.class).in(Singleton.class);
		bind(OptionTypeAdminServlet.class).in(Singleton.class);

		bind(AppSettingsServlet.class).in(Singleton.class);
		bind(AdminAuthFilter.class).in(Singleton.class);
		bind(AdminLoginServlet.class).in(Singleton.class);
	}		


}
