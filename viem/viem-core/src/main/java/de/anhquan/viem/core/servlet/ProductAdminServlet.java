package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.google.inject.Inject;

import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.DefaultProductDao;
import de.anhquan.viem.core.dao.OptionDao;
import de.anhquan.viem.core.dao.OptionItemDao;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.dao.ProductCategoryRelationDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.json.ProductImporter;
import de.anhquan.viem.core.model.Action;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.CategoryRender;
import de.anhquan.viem.core.model.Option;
import de.anhquan.viem.core.model.OptionItem;
import de.anhquan.viem.core.model.OptionType;
import de.anhquan.viem.core.model.Product;
import de.anhquan.viem.core.util.Parser;
import de.anhquan.viem.core.velocity.VelocityEngineManager;

/**
 * This servlet manages products in the backend
 * @author anhquan
 *
 */
@ServletPath(value = "/admin/product")
public class ProductAdminServlet extends BaseEntityAdminServlet<Product> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(ProductAdminServlet.class.getName());

	private CategoryDao categoryDao;
	private ProductCategoryRelationDao pcRelDao;
	private DefaultProductDao defaultProductDao;
	private OptionDao optionDao;
	private OptionTypeDao optionTypeDao;
	private OptionItemDao optionItemDao;
	
	@Inject
	public ProductAdminServlet( 
								final ProductDao productDao, 
								final CategoryDao categoryDao, 
								final ProductCategoryRelationDao pcRelDao,
								final DefaultProductDao defaultProductDao,
								final OptionDao optionDao,
								final OptionTypeDao optionTypeDao,
								final OptionItemDao optionItemDao,
								final ProductImporter jsonImporter) {
		super(productDao,jsonImporter);
		this.categoryDao = categoryDao;
		this.pcRelDao = pcRelDao;
		this.optionDao = optionDao;
		this.defaultProductDao = defaultProductDao;
		this.optionTypeDao = optionTypeDao;
		this.optionItemDao = optionItemDao;
	}

	@Override
	protected void doClearAll(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException, IOException {
		List<Product> products = entityDao.getAll();
		for (Product product : products) {
			pcRelDao.deleteCategoryOf(product);
		}
		
		super.doClearAll(request, response);
	}
	
	@Override
	protected void processExtraJsonRequest(Action action, Product product, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long optionItemId = Parser.parseLong(request.getParameter("optionItemId"));
	
		switch (action){
			case ADD_OPTION:
				doAddOption(product, response);
				return;
			case DUPLICATE_OPTION:
				doDuplicateOption(product, request, response);
				return;
				
			case DELETE_OPTION:
				doDeleteOption(product, request, response);
				return;
	
			case UPDATE_OPTION:
				doUpdateOption(request, response);
				return;
	
			case SORT_OPTION:
				
				doSortOptions(request, response);
				return;
	
			case ADD_OPTION_ITEM:
				doAddOptionItem(product, request, response);
				return;
				
			case DUPLICATE_OPTION_ITEM:
				doDuplicateOptionItem(product, request, response, optionItemId);
				return;
				
			case UPDATE_OPTION_ITEM:
				doUpdateOptionItem(request, response);
				return;
				
			case DELETE_OPTION_ITEM:
				doDeleteOptionItem(request, response, optionItemId);
				return;
				
			case SORT_OPTION_ITEM:
				
				doSortOptionItems(request, response);
				return;
		default:
			break;
		}

	}

	protected void doSortOptionItems(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sortedList = Parser.parseString(request.getParameter("sortedOptionItems"));
		optionItemDao.sort(sortedList);
		renderJson(response, ApplicationError.OK);
	}

	@SuppressWarnings("unchecked")
	protected void doDeleteOptionItem(HttpServletRequest request,
			HttpServletResponse response, Long optionItemId)
			throws ServletException, IOException {
		log.info("deleteOptionItem ....");
		Long optionId = Parser.parseLong(request.getParameter("optionId"));

		OptionItem optionItem = optionItemDao.get(optionItemId);
		Option option = optionDao.get(optionId);

		if ((optionItem == null) || (option == null)){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		else {
			option.removeOptionItem(optionItem);
			optionItemDao.delete(optionItemId);
			optionDao.put(option);
			
			jsonResult.put("optionItemId", optionItemId);
			renderJson(response, ApplicationError.OK);
		}
	}

	@SuppressWarnings("unchecked")
	protected void doUpdateOptionItem(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		Long optionItemId = Parser.parseLong(request.getParameter("optionItemId"));

		OptionItem optionItem = optionItemDao.get(optionItemId);
		if (optionItem == null){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		else {
			optionItem.setTitle(Parser.parseString(request.getParameter("title")));
			optionItem.setInternalComment(Parser.parseString(request.getParameter("internalComment")));
			optionItem.setImage(Parser.parseString(request.getParameter("image")));
			optionItem.setPrice(Parser.parseInt(request.getParameter("price")));
			optionItem.setWeight(Parser.parseInt(request.getParameter("weight")));
			boolean visible = Parser.parseBoolean(request.getParameter("visible"));
			optionItem.setVisible(visible );
			boolean isDefault = Parser.parseBoolean(request.getParameter("isDefault"));
			optionItem.setDefault(isDefault );
			optionItem.setSortValue(Parser.parseInt(request.getParameter("sortValue")));

			optionItemDao.put(optionItem);
			
			jsonResult.put("optionItemId", optionItemId);
			renderJson(response, ApplicationError.OK);
		}
	}

	protected void doDuplicateOptionItem(Product product,
			HttpServletRequest request, HttpServletResponse response,
			Long optionItemId) throws ServletException, IOException,
			ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		Long optionId = Parser.parseLong(request.getParameter("optionId"));

		Option option = optionDao.get(optionId);
		OptionItem optionItem = optionItemDao.get(optionItemId);
		if ((option==null) || (optionItem==null)){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		
		OptionItem newOptionItem = new OptionItem();
		newOptionItem.copyFrom(optionItem);
		newOptionItem.setId(null);
		newOptionItem.setTitle(newOptionItem.getTitle()+"-new");
		newOptionItem = optionItemDao.put(newOptionItem);
		option.addOptionItem(newOptionItem);
		optionDao.put(option);
		
		Template template = VelocityEngineManager.getTemplate("/admin/OptionItem.new.vm");
		VelocityContext ctx = new VelocityContext();
		Writer strWriter = new StringWriter();
		ctx.put("optionItem", newOptionItem);
		ctx.put("option", option);
		ctx.put("entity", product);
		ctx.put("basePath", this.getServletBasePath());

		template.merge(ctx, strWriter);
		
		jsonResult.put("newItem", strWriter.toString());
		renderJson(response, ApplicationError.OK);
	}

	protected void doAddOptionItem(Product product, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		Long optionId = Parser.parseLong(request.getParameter("optionId"));

		Option option = optionDao.get(optionId);
		if (option==null){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		
		OptionItem optionItem = new OptionItem();
		optionItem = optionItemDao.put(optionItem);
		option.addOptionItem(optionItem);
		optionDao.put(option);
		entityDao.put(product);
		
		Template template = VelocityEngineManager.getTemplate("/admin/OptionItem.new.vm");
		VelocityContext ctx = new VelocityContext();
		Writer strWriter = new StringWriter();
		ctx.put("optionItem", optionItem);
		ctx.put("option", option);
		ctx.put("entity", product);
		ctx.put("basePath", this.getServletBasePath());

		template.merge(ctx, strWriter);
		
		jsonResult.put("newItem", strWriter.toString());
		renderJson(response, ApplicationError.OK);
	}

	protected void doSortOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sortedList = Parser.parseString(request.getParameter("sortedOptions"));
		optionDao.sort(sortedList);
		renderJson(response, ApplicationError.OK);
	}

	protected void doUpdateOption(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Long optionId = Parser.parseLong(request.getParameter("optionId"));

		Option option = optionDao.get(optionId);
		if (option == null){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		else {
			option.setTitle(Parser.parseString(request.getParameter("title")));
			option.setIcon(Parser.parseString(request.getParameter("icon")));
			option.setType(Parser.parseString(request.getParameter("type")));
			option.setSortValue(Parser.parseInt(request.getParameter("sortValue")));		
			option.setDisplayType(Parser.parseInt(request.getParameter("displayType")));
			option.setVisible(Parser.parseBoolean(request.getParameter("visible")));
			option.setInternalComment(Parser.parseString(request.getParameter("internalComment")));
			
			optionDao.put(option);
			
			jsonResult.put("optionId", optionId);
			renderJson(response, ApplicationError.OK);
		}
	}

	protected void doDeleteOption(Product product, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		log.info("deleteOption ....");
		Long optionId = Parser.parseLong(request.getParameter("optionId"));
		Option option = optionDao.get(optionId);
		if (option == null){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}
		else {
			product.removeOption(option);
			optionDao.delete(option);
			entityDao.put(product);
			
			jsonResult.put("optionId", optionId);
			
			renderJson(response, ApplicationError.OK);
		}
	}

	protected void doDuplicateOption(Product product,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, ResourceNotFoundException,
			ParseErrorException, MethodInvocationException {
		log.info("duplicateOption ....");
		Long optionId = Parser.parseLong(request.getParameter("optionId"));
		Option option = optionDao.get(optionId);
		if (option == null){
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			return;
		}

		Option newOption = new Option();
		newOption.copyFrom(option);
		newOption.setId(null);
		newOption = optionDao.put(newOption);
		product.addOption(newOption);
		
		List<OptionItem> optionItems = option.getOptionItemsAsList();
		for (OptionItem optionItem : optionItems) {
			OptionItem newOptionItem = new OptionItem();
			newOptionItem.copyFrom(optionItem);
			newOptionItem.setId(null);
			newOptionItem = optionItemDao.put(newOptionItem);
			newOption.addOptionItem(newOptionItem);
			optionDao.put(newOption);
		}
		
		entityDao.put(product);
		
		Template template = VelocityEngineManager.getTemplate("/admin/Option.new.vm");
		VelocityContext ctx = new VelocityContext();
		Writer strWriter = new StringWriter();
		List<OptionType> optionTypes = optionTypeDao.getAll();
		ctx.put("optionTypes", optionTypes);
		ctx.put("option", newOption);
		ctx.put("entity", product);
		ctx.put("basePath", this.getServletBasePath());

		template.merge(ctx, strWriter);
		
		jsonResult.put("newItem", strWriter.toString());
		renderJson(response, ApplicationError.OK);
	}

	protected void doAddOption(Product product, HttpServletResponse response)
			throws ResourceNotFoundException, ParseErrorException,
			MethodInvocationException, ServletException, IOException {
		log.info("addOption ....");

		Option option = new Option();
		option = optionDao.put(option);
		product.addOption(option);
		
		entityDao.put(product);
		
		Template template = VelocityEngineManager.getTemplate("/admin/Option.new.vm");
		VelocityContext ctx = new VelocityContext();
		Writer strWriter = new StringWriter();
		List<OptionType> optionTypes = optionTypeDao.getAll();
		ctx.put("optionTypes", optionTypes);
		ctx.put("option", option);
		ctx.put("entity", product);
		ctx.put("basePath", this.getServletBasePath());

		template.merge(ctx, strWriter);
		jsonResult.put("newItem", strWriter.toString());
		renderJson(response, ApplicationError.OK);
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	protected void doDuplicateEntity(Product srcProduct, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		Product newProduct = entityDao.give();
		newProduct.copyFrom(srcProduct);
		newProduct.setId(null);
		newProduct.setTitle(srcProduct.getTitle()+" (dup)");
		newProduct.setName(srcProduct.getName()+"-dup");
		
		//copy option
		List<Option> options = srcProduct.getOptionsAsList();
		for (Option srcOption: options) {
			Option newOption = new Option();
			newOption.copyFrom(srcOption);
			List<OptionItem> srcOptionItems = srcOption.getOptionItemsAsList();
			for (OptionItem srcOptionItem : srcOptionItems) {
				OptionItem newOptionItem = new OptionItem();
				newOptionItem.copyFrom(srcOptionItem);
				newOptionItem.setId(null);
				optionItemDao.put(newOptionItem);
				newOption.addOptionItem(newOptionItem);
			}
			optionDao.put(newOption);
			newProduct.addOption(newOption);
		}
		entityDao.put(newProduct);
		entityDao.clearCache();	//Work around
		//entityDao.get(newProduct.getId());
		
		//copy categories
		List<Category> categories = pcRelDao.findCategoryOf(srcProduct);
		for (Category category : categories) {
			pcRelDao.addProductToCategory(newProduct, category);
		}
		
		// return value
		Template template = VelocityEngineManager.getTemplate("/admin/Product.new.vm");
		VelocityContext ctx = new VelocityContext();
	    Writer strWriter = new StringWriter();
	    ctx.put("entity", newProduct);
	    ctx.put("basePath", this.getServletBasePath());

		template.merge(ctx, strWriter);
		
		jsonResult.put("newItem", strWriter.toString());
		renderJson(response, ApplicationError.OK);

	}
	
	@Override
	protected void doViewEntity(Product product, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		List<Category> categories = categoryDao.getAll();
		List<Category> selectedCategories = pcRelDao.findCategoryOf(product);
		
		List<CategoryRender> renderCategories = new ArrayList<CategoryRender>();
		for (Category category : categories) {
			CategoryRender renderCat = new CategoryRender();
			renderCat.setId(category.getId());
			renderCat.setName(category.getName());
			renderCat.setTitle(category.getTitle());
			if (selectedCategories.contains(category))
				renderCat.setSelected("selected");
			else
				renderCat.setSelected("");
			renderCategories.add(renderCat);
		}
		
		context.put("categories", renderCategories);
		
		List<Option> options = product.getOptionsAsList();
		Collections.sort(options);
		
		context.put("options", options);
		
		List<OptionType> optionTypes = optionTypeDao.getAll();
		context.put("optionTypes", optionTypes);

		context.put("defaultOptions", defaultProductDao.getOptionsAsList());

		renderEditView(product, response);
	}


	@Override
	protected void afterUpdateEntity(Product product, HttpServletRequest request) {
		
		//update categories
		pcRelDao.deleteCategoryOf(product);
		String[] categories = request.getParameterValues("categories");
		
		if (categories!=null){
			for (String strId : categories) {
				try{
					Long id = Long.parseLong(strId);
					Category category = categoryDao.get(id);
					if (category!=null){
						log.info("found category : "+category.getTitle());
						pcRelDao.addProductToCategory(product, category);
					}
				}
				catch (NumberFormatException e) {
				}
			}
		}
	}

	@Override
	protected void doViewList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Product> products;
		String filter = Parser.parseString(request.getParameter("filter"));
		if (filter.isEmpty()){
			products = entityDao.getAll();
			context.put("filterBy", "All ("+products.size()+")");
		}
		else{
			Category category = categoryDao.findFirstItemWithName(filter);
			products = pcRelDao.findProductOf(category);
			if (category!=null)
				context.put("filterBy", category.getTitle()+" ("+products.size()+")");
			else
				context.put("filterBy", "Unknown Category");
		}
		
		List<Category> categories = categoryDao.getAll();
		Collections.sort(categories);
		context.put("categories", categories);
		
		Collections.sort(products);
		renderListView(products, response);
	}
	
	@Override
	public void onProcessFileUploadSuccess(String uploadedContent) {
		log.info("onProcessFileUploadSuccess: "+uploadedContent);
		try {
			jsonImporter.parse(uploadedContent);
		} catch (ParseJSONException e) {
			onProcessFileUploadError(-1, e.getMessage());
		}
	}
	
	@Override
	protected void doDeleteEntity(Product product, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		List<Option> options = product.getOptionsAsList();
		for (Option option : options) {
			List<OptionItem> optionItems = option.getOptionItemsAsList();
			for (OptionItem optionItem : optionItems) {
				optionItemDao.delete(optionItem);
			}
			log.info("delete option "+option.getId());
			optionDao.delete(option);
		}
		log.info("delete product: "+product.getId());
		entityDao.delete(product);

		renderJson(response, ApplicationError.OK);
		return;		
	}
}
