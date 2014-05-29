package de.anhquan.viem.frontend.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.servlet.AbstractServlet;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.model.Product;

@ServletPath(basePath = "/product/*")
public class ProductServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	ProductManager manager;

	@Inject
	public ProductServlet(AppSettingManager settingManager,
			ProductManager manager) {
		super(settingManager);
		this.manager = manager;
	}

	public static final Logger log = Logger.getLogger(ProductServlet.class
			.getName());

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = StringUtils.trimToEmpty(request.getRequestURI());
		log.info("Request product : path="+path);

		int i = "/product/".length();
		String sku = path.substring(i);

		Product entity = manager.findByName(sku);
		if (entity==null){
			log.info("Product Not Found");
			renderHtml(response, currentTheme+"/pages/404.vm");
			return;
		}
		
		log.info("Render product sku = "+entity.getName());
		context.put("entity", entity);
		
		String template = "default";
		if (!StringUtils.isEmpty(entity.getTemplate())){
			template = entity.getTemplate();
		}
		
		renderHtml(response, currentTheme+"/pages/product."+template+".vm");
	}

}
