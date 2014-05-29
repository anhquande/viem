package de.anhquan.viem.frontend.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.servlet.AbstractServlet;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.dao.cache.ProductCategoryManager;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.model.Product;
import de.anhquan.viem.frontend.model.ProductCategory;

@ServletPath(basePath = "/category/*")
public class ProductCategoryServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	ProductCategoryManager manager;
	ProductManager productManager;

	@Inject
	public ProductCategoryServlet(AppSettingManager settingManager,
			ProductCategoryManager manager, ProductManager productManager) {
		super(settingManager);
		this.manager = manager;
		this.productManager = productManager;
	}

	public static final Logger log = Logger.getLogger(ProductCategoryServlet.class
			.getName());

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = StringUtils.trimToEmpty(request.getRequestURI());

		int i = "/category/".length();
		String categoryName = path.substring(i);

		ProductCategory entity = manager.findByName(categoryName);
		if (entity==null){
			renderHtml(response, currentTheme+"/pages/404.vm");
			return;
		}
		
		context.put("entity", entity);
		
		List<Product> products = productManager.findByCategory(categoryName);

		context.put("products", products);
		
		String template = "default";
		if (!StringUtils.isEmpty(entity.getTemplate())){
			template = entity.getTemplate();
		}
		
		renderHtml(response, currentTheme+"/pages/category."+template+".vm");
	}

}
