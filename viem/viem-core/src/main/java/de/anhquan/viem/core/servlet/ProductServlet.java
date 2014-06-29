package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.Constants;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.DefaultProductDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionItemDao;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.Page;
import de.anhquan.viem.core.model.Product;

@ServletPath(value = "/product/*")
public class ProductServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	
	ProductDao productDao;

	final CategoryDao categoryDao; 
	final ProductCategoryRelationDao pcRelDao;
	final DefaultProductDao defaultProductDao;
	final OptionDao optionDao;
	final OptionTypeDao optionTypeDao;
	final OptionItemDao optionItemDao;
	
	@Inject
	public ProductServlet(
			final ProductDao productDao,
			final CategoryDao categoryDao, 
			final ProductCategoryRelationDao pcRelDao,
			final DefaultProductDao defaultProductDao,
			final OptionDao optionDao,
			final OptionTypeDao optionTypeDao,
			final OptionItemDao optionItemDao) {
		this.productDao = productDao;
		this.categoryDao = categoryDao;
		this.pcRelDao = pcRelDao;
		this.defaultProductDao = defaultProductDao;
		this.optionDao = optionDao;
		this.optionTypeDao = optionTypeDao;
		this.optionItemDao = optionItemDao;
		
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
		
		List<Category> categories = categoryDao.getVisibleCategories();
		context.put("categories", categories);
		
		String path = StringUtils.trimToEmpty(request.getRequestURI());

		int i = "/product/".length();
		String sku = path.substring(i);

		Product product = productDao.findFirstItemWithName(sku);
		if (product==null){
			replyPageNotFound(response);
			return;
		}
		
		if (!product.isVisible()){
			replyPageNotFound(response);
			return;
		}
		
		context.put("product", product);
		
		List<Category> selectedCategories = pcRelDao.findCategoryOf(product);	
		context.put("productCategories", selectedCategories);
		
		List<Option> options = product.getOptionsAsList();
		Collections.sort(options);
		
		context.put("productOptions", options);

		String template = "default";
		if (!StringUtils.isEmpty(product.getTemplate())){
			template = product.getTemplate();
		}
		
		Page page = new Page();
		page.setTitle(product.getTitle());
		page.setDescription(product.getShortDescription());
		page.setName(getServletBasePath().replaceFirst("\\*", product.getName()));
		context.put("page", page);
		context.put("url", config.getSiteUrl()+page.getName());
		context.put("currentCategoryId", request.getSession().getAttribute(Constants.SESSION_CURRENT_CATEGORY));
		
		renderHtml(response, currentTheme+"/pages/product."+template+".vm");
	}

}
