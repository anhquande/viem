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
import de.anhquan.viem.frontend.dao.ProductCategoryDao;
import de.anhquan.viem.frontend.dao.cache.ProductCategoryManager;
import de.anhquan.viem.frontend.dao.cache.ProductManager;
import de.anhquan.viem.frontend.model.Product;
import de.anhquan.viem.frontend.model.ProductCategory;

@ServletPath(basePath="/admin/category")
public class ProductCategoryAdminServlet extends BaseEntityAdminServlet<ProductCategory> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(ProductCategoryAdminServlet.class
			.getName());

	ProductManager productManager;
	@Inject
	public ProductCategoryAdminServlet(AppSettingManager settingManager, ProductCategoryManager productCategoryManager, ProductCategoryDao productCategoryDao, ProductManager productManager) {
		super(settingManager, productCategoryManager, productCategoryDao);
		this.productManager = productManager;
	}

	@Override
	protected void doUpdateEntity(ProductCategory entity, HttpServletRequest request) {
		entity.setTitle(StringUtils.trimToEmpty(request.getParameter("title")));
		entity.setFullDescription(StringUtils.trimToEmpty(request.getParameter("fulldescription")));
		entity.setShortDescription(StringUtils.trimToEmpty(request.getParameter("shortdescription")));
		entity.setTemplate(StringUtils.trimToEmpty(request.getParameter("template")));
		entity.setThumbnail(StringUtils.trimToEmpty(request.getParameter("thumbnail")));
		entity.setImage1(StringUtils.trimToEmpty(request.getParameter("image1")));
		entity.setImage2(StringUtils.trimToEmpty(request.getParameter("image2")));
		entity.setImage3(StringUtils.trimToEmpty(request.getParameter("image3")));
		entity.setSortValue(parseToZero(request.getParameter("sortvalue")));
		entity.setVisible(parseBoolean(request.getParameter("visible")));
	}

	@Override
	protected ProductCategory doCreateEntity(HttpServletRequest request) {
		ProductCategory entity = new ProductCategory();		
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
		entity.setThumbnail(StringUtils.trimToEmpty(request.getParameter("thumbnail")));
		entity.setImage1(StringUtils.trimToEmpty(request.getParameter("image1")));
		entity.setImage2(StringUtils.trimToEmpty(request.getParameter("image2")));
		entity.setImage3(StringUtils.trimToEmpty(request.getParameter("image3")));
		entity.setSortValue(parseToZero(request.getParameter("sortvalue")));
		entity.setVisible(parseBoolean(request.getParameter("visible")));

		return entity;
	}
	
	@Override
	protected void doView(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String categoryName = StringUtils.trimToEmpty(request
				.getParameter("name"));
		ProductCategory category = manager.findByName(categoryName);
		List<Product> products = productManager.findByCategory(categoryName);
		context.put("products", products);
		
		renderEditView(category, response);
	}
}