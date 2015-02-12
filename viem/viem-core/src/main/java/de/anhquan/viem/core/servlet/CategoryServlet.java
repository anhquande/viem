package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
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
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Page;
import de.anhquan.viem.core.model.Product;

@ServletPath(value = "/category/*")
public class CategoryServlet extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	CategoryDao categoryDao;
	ProductDao productDao;
	ProductCategoryRelationDao pcRelDao;

	@Inject
	public CategoryServlet(CategoryDao productCategoryDao, ProductDao productDao, ProductCategoryRelationDao pcRelDao) {
		this.categoryDao = productCategoryDao;
		this.productDao = productDao;
		this.pcRelDao = pcRelDao;
	}

	public static final Logger log = Logger.getLogger(CategoryServlet.class
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

		int i = "/category/".length();
		String categoryName = path.substring(i);

		Category category = categoryDao.findFirstItemWithName(categoryName);
		if (category==null){
			renderHtml(response, currentTheme+"/pages/404.vm");
			return;
		}
		if (!category.isVisible()){
			replyPageNotFound(response);
			return;
		}

		request.getSession().setAttribute(Constants.SESSION_CURRENT_CATEGORY, category.getId());
		context.put("currentCategoryId", category.getId());
		context.put("entity", category);
		
		List<Product> products = pcRelDao.findProductOf(category);
		List<Product> visibleProducts = new LinkedList<Product>();
		for (Product product : products) {
			if (product.isVisible())
				visibleProducts.add(product);
		}
		Collections.sort(visibleProducts);
		context.put("products", visibleProducts);
		
		String template = "default";
		if (!StringUtils.isEmpty(category.getTemplate())){
			template = category.getTemplate();
		}
		
		Page page = new Page();
		page.setTitle(category.getTitle());
		page.setDescription(category.getShortDescription());
		page.setName(getServletBasePath().replaceFirst("\\*", category.getName()));
		context.put("page", page);
		context.put("url", config.getSiteUrl()+page.getName());

		renderHtml(response, currentTheme+"/pages/category."+template+".vm");
	}

}
