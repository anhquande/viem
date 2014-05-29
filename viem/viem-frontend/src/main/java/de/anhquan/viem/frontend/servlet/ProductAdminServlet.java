package de.anhquan.viem.frontend.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.dao.ProductDao;
import de.anhquan.viem.frontend.dao.cache.ProductCategoryManager;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.model.Product;
import de.anhquan.viem.frontend.model.ProductCategory;

@ServletPath(basePath="/admin/product")
public class ProductAdminServlet extends BaseEntityAdminServlet<Product> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(ProductAdminServlet.class
			.getName());

	private ProductCategoryManager categoryManager;
	@Inject
	public ProductAdminServlet(AppSettingManager settingManager, ProductManager productManager, ProductDao productDao, ProductCategoryManager categoryManager) {
		super(settingManager, productManager, productDao);
		this.categoryManager = categoryManager;
	}

	@Override
	protected void doUpdateEntity(Product product, HttpServletRequest request) {
		product.setTitle(StringUtils.trimToEmpty(request
				.getParameter("title")));
		product.setFullDescription(StringUtils.trimToEmpty(request
				.getParameter("fulldescription")));
		product.setShortDescription(StringUtils.trimToEmpty(request
				.getParameter("shortdescription")));
		product.setTemplate(StringUtils.trimToEmpty(request
				.getParameter("template")));
		product.parseProductCategories(StringUtils.trimToEmpty(request
				.getParameter("productcategories")));
		product.setThumbnail(StringUtils.trimToEmpty(request
				.getParameter("thumbnail")));
		product.setImage1(StringUtils.trimToEmpty(request
				.getParameter("image1")));
		product.setImage2(StringUtils.trimToEmpty(request
				.getParameter("image2")));
		product.setImage3(StringUtils.trimToEmpty(request
				.getParameter("image3")));
		product.setBasePrice(parseToZero(request.getParameter("baseprice")));
		product.setComparePrice(parseToZero(request.getParameter("compareprice")));		
		product.setSortValue(parseToZero(request.getParameter("sortvalue")));
		product.setQuantity(parseToZero(request.getParameter("quantity")));

	}

	@Override
	protected Product doCreateEntity(HttpServletRequest request) {
		Product entity = new Product();		
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM HH:mm:sss");
		String now = df.format(new Date());
		entity.setTitle("New Entity -"+now);
		String name= StringUtils.trimToEmpty(request.getParameter("name"));
		if (name.isEmpty())
			name = "newentity-"+now;			
		entity.setName(name);
		
		String title = StringUtils.trimToEmpty(request.getParameter("title"));
		if (title.isEmpty())
			title = "New Entity - "+now;
		entity.setTitle(title);
		
		String template = StringUtils.trimToEmpty(request.getParameter("template"));
		if (template.isEmpty())
			template = "default";
		entity.setTemplate(template);
		
		entity.setFullDescription(StringUtils.trimToEmpty(request.getParameter("fulldescription")));
		entity.setShortDescription(StringUtils.trimToEmpty(request.getParameter("shortdescription")));
		entity.parseProductCategories(StringUtils.trimToEmpty(request.getParameter("productCategories")));
		entity.setThumbnail(StringUtils.trimToEmpty(request.getParameter("thumbnail")));
		entity.setImage1(StringUtils.trimToEmpty(request.getParameter("image1")));
		entity.setImage2(StringUtils.trimToEmpty(request.getParameter("image2")));
		entity.setImage3(StringUtils.trimToEmpty(request.getParameter("image3")));
		entity.setBasePrice(parseToZero(request.getParameter("baseprice")));
		entity.setComparePrice(parseToZero(request.getParameter("compareprice")));
		entity.setSortValue(parseToZero(request.getParameter("sortvalue")));
		entity.setQuantity(parseToZero(request.getParameter("quantity")));

		return entity;
	}

	@Override
	protected void doViewList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Product> entities;
		String filterBy = StringUtils.trimToEmpty(request.getParameter("filter"));
		if ((filterBy.isEmpty()) || ("all".compareToIgnoreCase(filterBy)==0)){
			entities = manager.getAll();
			context.put("filterBy", "All ("+entities.size()+")");
		}
		else{
			entities = ((ProductManager)manager).findByCategory(filterBy);
			ProductCategory category = categoryManager.findByName(filterBy);
			if (category!=null)
				context.put("filterBy", category.getTitle()+" ("+entities.size()+")");
			else
				context.put("filterBy", "Unknown Category");
		}
		
		context.put("categories", categoryManager.getAll());
		renderListView(entities, response);
	}

}
