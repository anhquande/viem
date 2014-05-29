package de.anhquan.viem.frontend.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.NameBasedDao;
import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.dao.cache.CacheManager;
import de.anhquan.viem.core.model.NameBasedEntity;
import de.anhquan.viem.core.servlet.AbstractServlet;

public abstract class BaseEntityAdminServlet<Entity extends NameBasedEntity> extends AbstractServlet {

	private static final long serialVersionUID = 1L;
	protected final String editTemplateFile;
	protected final String listTemplateFile;
	protected CacheManager<Entity> manager;
	protected NameBasedDao<Entity> dao;
	protected String className;

	public static final Logger log = Logger.getLogger(BaseEntityAdminServlet.class
			.getName());
	
	@Inject
	public BaseEntityAdminServlet(AppSettingManager settingManager,
			CacheManager<Entity> manager, NameBasedDao<Entity> dao) {
		super(settingManager);
		
		this.manager = manager;
		this.dao = dao;
		className = manager.createObject().getClass().getSimpleName();
		editTemplateFile = "/admin/"+className+".edit.vm";
		listTemplateFile = "/admin/"+className+".list.vm";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StringUtils.trimToEmpty(request.getParameter("action"));
		String entityName = StringUtils.trimToEmpty(request.getParameter("name"));
		log.info("Jsonrequest. .. action = " + action);

		if ("showall".compareToIgnoreCase(action) == 0) {
			Map retValue = new HashMap();
			retValue.put("entities", manager.getAll());
			replyOk(response, retValue);
			return;
		}
		// Load from database to cache
		if ("loadall".compareToIgnoreCase(action) == 0) {
			manager.readFromDataStore();
			replyOk(response, null);
			return;
		}

		// Save cache to database
		if ("saveall".compareToIgnoreCase(action) == 0) {
			manager.writeToDataStore();
			replyOk(response, null);
			return;
		}

		if ("clearall".compareToIgnoreCase(action) == 0) {
			List<Entity> entities = dao.getAll();
			dao.delete(entities);
			manager.clearCache();
			replyOk(response, null);
			return;
		}
				
		if ("delete".compareToIgnoreCase(action) == 0) {
			Entity entity = manager.findByName(entityName);
			if (entity == null) {
				log.info("Entity Not Found");
				JSONObject json = new JSONObject();
				json.put("errno", -1);
				json.put("errmsg", "Entity Not Found");
				json.put("entityName", entityName);
				renderJson(response, json);
				return;
			} 
			
			manager.delete(entity);

			Entity found = dao.findFirstItemWithName(entityName);
			if (found != null) {
				dao.delete(found);
			}

			Map retValue = new HashMap();
			retValue.put("entityName", entityName);
			replyOk(response, retValue);
			return;
		}

		if ("sort".compareToIgnoreCase(action) == 0) {
			String newItems  = StringUtils.trimToEmpty(request.getParameter("newItems"));
			manager.sort(newItems);
			replyOk(response, null);
		}
		if ("update".compareToIgnoreCase(action) == 0) {
			log.info("try to update the entity name = "+entityName);
			Entity entity = manager.findByName(entityName);
			if (entity == null) {
				log.info("Entity Not Found");
				JSONObject json = new JSONObject();
				json.put("errno", -1);
				json.put("errmsg", "Entity Not Found");
				json.put("entityName", entityName);
				renderJson(response, json);
				return;
			} 
			
			String newName = StringUtils.trimToEmpty(request
					.getParameter("newname"));

			log.info("newname = "+newName);
			if (entityName.compareTo(newName) != 0) {
				log.info("the newname is different from the current name");
				if (manager.findByName(newName) != null) {
					// avoid duplicate page name
					log.info("the newname is already in the cache");
					JSONObject json = new JSONObject();
					json.put("errno", -2);
					json.put("errmsg", "Duplicated entity name");
					json.put("entityName", newName);
					renderJson(response, json);
					return;
				}

				log.info("update the entity name to >"+newName);
				entity.setName(newName);
			}
			
			doUpdateEntity(entity, request);

			Map retValue = new HashMap();
			retValue.put("entityName", newName);
			replyOk(response, retValue);
			
			return;
		}
	}

	protected abstract void doUpdateEntity(Entity entity, HttpServletRequest request);
	protected abstract Entity doCreateEntity(HttpServletRequest request);

	protected int parseToZero(String s){
		if (s == null)
			return 0;
		
		s = StringUtils.trimToEmpty(s);
		int p = 0;
		try{
			p = Integer.parseInt(s);
		}
		catch (Exception e){}
		return p;
	}

	/**
	 * 
	 * @param s s==0 	-> 	false
	 * 			s==1	->	true
	 * 			s=='false' 	-> false
	 * 			s=='true' 	-> true
	 * @return
	 */
	protected boolean parseBoolean(String s){
		if (s == null)
			return false;
		
		s = StringUtils.trimToEmpty(s);
		boolean p = false;
		
		try{
			int i = Integer.parseInt(s);
			return i==1;
		}
		catch (Exception e){}
		
		try{
			p = Boolean.parseBoolean(s);
		}
		catch (Exception e){}
		return p;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void replyOk(HttpServletResponse response, Map data)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		if (data != null)
			json.putAll(data);

		json.put("errno", 0);
		json.put("errmsg", "OK");

		renderJson(response, json);
	}
	
	protected void renderEditView(Entity entity, HttpServletResponse response) throws ServletException, IOException{
		context.put("entity", entity);
		context.put("className", className);
		context.put("basePath", servletBasePath);
		renderHtml(response, editTemplateFile);
	}
	
	protected void renderListView(List<Entity> entities, HttpServletResponse response) throws ServletException, IOException{
		context.put("entities", entities);
		context.put("className", className);
		context.put("basePath", servletBasePath);		
		renderHtml(response, listTemplateFile);
	}
	
	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StringUtils.trimToEmpty(request.getParameter("action"));

		if ("view".compareToIgnoreCase(action) == 0) {
			doView(request, response);
			return;
		}

		if ("export2json".compareToIgnoreCase(action) == 0) {
			doExport(response);
			return;
		}

		if ("create".compareToIgnoreCase(action) == 0) {
			Entity entity = doCreateEntity(request);

			manager.put(entity);
			renderEditView(entity, response);
			return;
		}

		//show all 
		doViewList(request, response);
	}

	protected void doViewList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Entity> entities = manager.getAll();
		renderListView(entities, response);
	}

	protected void doExport(HttpServletResponse response)
			throws ServletException, IOException {
		manager.writeToDataStore();
		response.addHeader("Content-disposition",
				"attachment; filename="+this.getClass().getSimpleName()+".json");
		List<Entity> entities = dao.getAll();
		renderText(response, exportJSONString(entities));
	}

	protected void doView(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String entityName = StringUtils.trimToEmpty(request
				.getParameter("name"));
		Entity entity = manager.findByName(entityName);
		renderEditView(entity, response);
	}

	protected String exportJSONString(List<Entity> entities) {
		StringBuilder str = new StringBuilder();
		str.append("[");
		int i=0;
		int len = entities.size();
		for (Entity entity : entities) {
			str.append(entity.toJSON());
			i++;
			if (i<len)
				str.append(",");
		}
		str.append("]");
		return str.toString();
	}

	public void onProcessFileUploadSuccess(String uploadedContent) {
		log.info("onProcessFileUploadSuccess: "+uploadedContent);

		JSONParser parser = new JSONParser();
		List<Entity> entities = new ArrayList<Entity>();
		try {
			log.info("----------- parse JSON ... ---------");
			Object obj = parser.parse(uploadedContent);
			JSONArray json = (JSONArray) obj;
			
			log.info("after parsing: size ="+json.size());
			for(int i=0;i<json.size();i++){
				JSONObject item = (JSONObject) json.get(i);
				Entity p = manager.createObject();
				p.fromJSON(item);
				log.info("import: "+p.toString());
				entities.add(p);
			}
		} catch (ParseException e) {
			onProcessFileUploadError(-3, "Invalid JSON on the uploaded content.Content = "+uploadedContent);
		} catch (ClassCastException e){
			onProcessFileUploadError(-4, "Uploaded content must contain an array of entities. Content = "+uploadedContent);
		}
		
		if (entities.isEmpty()){
			onProcessFileUploadError(-5, "There is no entity to import. Content = "+uploadedContent);
		}
		else{
			//import to database;
			for(Entity p : entities){
				dao.put(p);
			}
		}
	}


}
