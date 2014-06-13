package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.json.CategoryImporter;
import de.anhquan.viem.core.model.Action;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.CategoryRender;
import de.anhquan.viem.core.model.Product;
import de.anhquan.viem.core.util.Parser;

@ServletPath(value="/admin/category")
public class CategoryAdminServlet extends BaseEntityAdminServlet<Category> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(CategoryAdminServlet.class
			.getName());

	ProductDao productDao;
	ProductCategoryRelationDao pcRelDao;
	
	@Inject
	public CategoryAdminServlet(AppSettingDao appSettingDao, 
			CategoryDao productCategoryDao, 
			ProductDao productDao, 
			ProductCategoryRelationDao pcRelDao, 
			CategoryImporter jsonImporter) {
		super(appSettingDao, productCategoryDao, jsonImporter);
		this.productDao = productDao;
		this.pcRelDao = pcRelDao;
	}

	@Override
	protected void doClearAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		pcRelDao.clearAll();
		List<Product> products = productDao.getAll();
		for (Product product : products) {
			product.clearCategories();
		}
		super.doClearAll(request, response);
	}
	
	@Override
	protected void afterUpdateEntity(Category entity, HttpServletRequest request) {
		

		entity.clearSubCategories();
		
		String[] subCategoryIds = request.getParameterValues("subCategories");
		
		if (subCategoryIds!=null) {
			for (String strId : subCategoryIds) {
				try{					
					Long id = Long.parseLong(strId);
					
					Category category = entityDao.get(id);
					if (category!=null)
						entity.addSubCategory(category);
					
				} catch (NumberFormatException e) {
				}
			}
		}
		
	}

	@Override
	protected void processExtraJsonRequest(Action action, Category category,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (action == Action.SAVE_SELECTED_PRODUCTS){
			String name = Parser.parseString(request.getParameter("name"));
			pcRelDao.deleteProductOf(category);
			String[] selectedProducts = request.getParameterValues("selectedProducts");
			if (selectedProducts!=null){
				
				for (String strProductId : selectedProducts) {
					log.info("Add product "+strProductId + " to category "+name);
					try{
						Long productId =Parser.parseLong(strProductId);
						if (productId>0){
							Product product = productDao.get(productId);
							if (product!=null){
								pcRelDao.addProductToCategory(product, category);
							}
						}
					}
					catch (NumberFormatException e) {
					}
				}
				
			}
			
			renderJson(response, ApplicationError.OK);
		}
	}
	
	@Override
	protected void doViewEntity(Category entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// Add Child Products to context 
		List<Product> products = pcRelDao.findProductOf(entity);
		Collections.sort(products);
		context.put("products", products);
		
		List<Product> allProducts = productDao.getAll();
		Collections.sort(products);
		context.put("allProducts", allProducts);
		
		// Add Sub Categories to context
		List<Category> subCategories = entity.getSubCategoriesAsList();
		Collections.sort(subCategories);
		List<Category> allCategories = entityDao.getAll();
		
		List<CategoryRender> renderCategories = new ArrayList<CategoryRender>();
		for (Category cat : allCategories) {
			if (!cat.equals(entity)){
				CategoryRender renderCat = new CategoryRender();
				renderCat.setId(cat.getId());
				renderCat.setName(cat.getName());
				renderCat.setTitle(cat.getTitle());
				if (subCategories.contains(cat))
					renderCat.setSelected("selected");
				else
					renderCat.setSelected("");
				renderCategories.add(renderCat);
			}
		}
		
		context.put("categories", renderCategories);
		renderEditView(entity, response);
	}

}