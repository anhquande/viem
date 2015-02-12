package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.dao.NameBasedDao;
import de.anhquan.viem.core.json.JSONImporter;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.model.Action;
import de.anhquan.viem.core.model.EntityNotFoundException;
import de.anhquan.viem.core.model.InvalidParameterException;
import de.anhquan.viem.core.model.NameBasedEntity;

public abstract class BaseEntityAdminServlet<Entity extends NameBasedEntity> extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	protected final String editTemplateFile;
	protected final String listTemplateFile;
	protected final NameBasedDao<Entity> entityDao;
	protected final String entityClassName;
	protected final JSONImporter<Entity> jsonImporter;

	public static final Logger log = Logger.getLogger(BaseEntityAdminServlet.class
			.getName());
	
	public BaseEntityAdminServlet(NameBasedDao<Entity> entityDao, JSONImporter<Entity> jsonImporter) {
		this.jsonImporter = jsonImporter;
		this.entityDao = entityDao;
		this.entityClassName = entityDao.getEntityClassName();
		this.editTemplateFile = "/admin/"+entityClassName+".edit.vm";
		this.listTemplateFile = "/admin/"+entityClassName+".list.vm";
	}

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, EntityNotFoundException, InvalidParameterException {
		Action action = Action.parse(request.getParameter("action"));
		log.info("processJsonRequest: action = "+action);
		Entity entity = null;
		if (action.requireId()){
			Long id = readId(request);
			entity = entityDao.get(id);
		}
		
		switch(action){
			case SHOW_ALL:
					doShowAll(request, response);
					break;
					
			case CLEAR_ALL:
					doClearAll(request, response);
					break;
					
			case SORT:
					doSort(request, response);
					break;
					
			case DELETE:
					doDeleteEntity(entity, request, response);
					break;
					
			case UPDATE:
					doUpdateEntity(entity, request, response);
					break;
					
			case DUPLICATE:
					doDuplicateEntity(entity, request, response);
					break;
					
			case TOGGLE_VISIBILITY:
					doToggleVisibility(entity, request, response);
					break;
			default:
					processExtraJsonRequest(action, entity, request,response);				
		}
	}
	
	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, InvalidParameterException, EntityNotFoundException {
		log.info("processHtmlRequest ...");
		Action action = Action.parse(request.getParameter("action"));
		log.info("action = "+action);
		Entity entity = null;
		if (action.requireId()){
			Long id = readId(request);
			
			if (id==0)
				throw new InvalidParameterException("id");
			
			entity = entityDao.get(id);
			if (entity == null)
				throw new EntityNotFoundException();
		}
		
		switch(action){
			case VIEW:				
				doViewEntity(entity, request, response);
				break;
				
			case EXPORT2JSON:
				doExport(response);
				break;
				
			case CREATE:
				doCreateEntity(request, response);
				break;
			
			case INIT:
				doInitEntity(request, response);
				
			default:
				doViewList(request, response);
		}
	}

	protected void doInitEntity(HttpServletRequest request,
			HttpServletResponse response) {
		
	}

	@SuppressWarnings("unchecked")
	protected void doUpdateEntity(Entity entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ApplicationError validatedResult = validateUpdate(entity, request);
		if (validatedResult != ApplicationError.OK){
			log.info("duplicated name - not update - return now");
			renderJson(response, validatedResult);
			return;
		}
		
		log.info("update now ...");
		entity.copyFrom(request);
		afterUpdateEntity(entity, request);
		
		entityDao.put(entity);
		
		jsonResult.put("id", entity.getId());
		renderJson(response, ApplicationError.OK);
	}

	/**
	 * Check if the request is valid, e.g. to check if the requested entity is duplicated
	 * @param request
	 * @return Return true if the request is valid, otherwise false
	 */
	protected ApplicationError validateUpdate(Entity entity, HttpServletRequest request) {
		
		String name = request.getParameter("name");
		log.info("Validate update: "+name+ " - compare to entity name = "+entity.getName());
		if (name == null)
			return ApplicationError.OK;
		if (name.compareTo(entity.getName()) == 0)
			return ApplicationError.OK;
		
		if (entityDao.findFirstItemWithName(name)!=null)
			return ApplicationError.DUPLICATED_NAME;
		
		return ApplicationError.OK;
	}

	protected void doSort(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sortedList = StringUtils.trimToEmpty(request.getParameter("sortedIds"));
		log.info("Sort entities ... id="+sortedList);
		entityDao.sort(sortedList);
		renderJson(response, ApplicationError.OK);
	}

	@SuppressWarnings("unchecked")
	protected void doShowAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		jsonResult.put("entities", entityDao.getAll());
		renderJson(response, ApplicationError.OK);
	}

	@SuppressWarnings("unchecked")
	protected void doToggleVisibility(Entity entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, SecurityException {		
		log.info("doToggleVisibility ...");

		if (entity.fieldExists("visible")){
			boolean val = !(boolean)entity.getValue("visible");
			entity.setValue("visible", val);
			entityDao.put(entity);
			jsonResult.put("id", entity.getId());
			jsonResult.put("visible", val);
			renderJson(response, ApplicationError.OK);
		}
		else{
			jsonResult.put("detailedErrmsg", "This entity contains no 'visible' property.");
			renderJson(response, ApplicationError.ACTION_FAILED);
		}		
	}

	@SuppressWarnings("unchecked")
	protected void doDuplicateEntity(Entity entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Entity newEntity = entityDao.give();
		newEntity.copyFrom(entity);
		newEntity.setId(null);
		entityDao.put(newEntity);
		jsonResult.put("id", newEntity.getId());
		renderJson(response, ApplicationError.OK);
	}

	@SuppressWarnings({ "unchecked" })
	protected void doDeleteEntity(Entity entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {	
		entityDao.delete(entity);
		jsonResult.put("id", entity.getId());
		renderJson(response, ApplicationError.OK);
	}

	protected void doClearAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		entityDao.clearAll();
		renderJson(response, ApplicationError.OK);		
	}

	protected void processExtraJsonRequest(Action action, Entity entity, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
	
	protected void afterUpdateEntity(Entity entity, HttpServletRequest request){
	}

	protected void renderEditView(Entity entity, HttpServletResponse response) throws ServletException, IOException{
		context.put("entity", entity);
		context.put("className", entityClassName);
		context.put("basePath", servletBasePath);
		renderHtml(response, editTemplateFile);
	}
	
	protected void renderListView(List<Entity> entities, HttpServletResponse response) throws ServletException, IOException{
		Collections.sort(entities);
		context.put("entities", entities);
		context.put("className", entityClassName);
		context.put("basePath", servletBasePath);		
		renderHtml(response, listTemplateFile);
	}
	
	protected void doCreateEntity(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Entity entity;
		log.info("Create new entity ...");
		entity = entityDao.give();
		entityDao.put(entity);
		
		String url=getServletBasePath()+"?action="+Action.VIEW+"&id="+entity.getId();
		log.info("Redirect to "+url);
		response.sendRedirect(url);
	}

	protected void doViewList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Entity> entities = entityDao.getAll();
		renderListView(entities, response);
	}

	protected void doExport(HttpServletResponse response)
			throws ServletException, IOException {
		response.addHeader("Content-disposition",
				"attachment; filename="+this.entityClassName+".json");
		renderText(response, entityDao.toJSONString());
	}

	protected void doViewEntity(Entity entity, HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		renderEditView(entity, response);
	}
	
	protected void onInvalidParam(ApplicationError error, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void onEntityNotFound(Long id, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	public void onProcessFileUploadSuccess(String uploadedContent) {
		log.info("onProcessFileUploadSuccess: "+uploadedContent);

		try {
			List<Entity> entities = jsonImporter.parse(uploadedContent);
			for(Entity p : entities){
				entityDao.put(p);				
			}

		} catch (ParseJSONException e) {
			onProcessFileUploadError(-1, e.getMessage());
		}
	}

}
