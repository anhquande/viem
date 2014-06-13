package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.json.AppSettingImporter;
import de.anhquan.viem.core.json.ParseJSONException;
import de.anhquan.viem.core.model.AppSetting;

@ServletPath(value="/admin/setting")
public class AppSettingsServlet extends AbstractServlet {

	private static final long serialVersionUID = 8191489816403471099L;
	public static final Logger log = Logger.getLogger(AppSettingsServlet.class.getName());
	
	@Inject
	public AppSettingsServlet(AppSettingDao appSettingDao){
		super(appSettingDao);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		appSettingDao.createDefaultSettings();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = StringUtils.trimToEmpty(request.getParameter("action"));		
		String key = StringUtils.trimToEmpty(request.getParameter("key"));
		String value = StringUtils.trimToEmpty(request.getParameter("value"));
		AppSetting setting = appSettingDao.findByKey(key);

		if ("clearAll".compareToIgnoreCase(action)==0){
			appSettingDao.clearAll();
			renderJson(response, ApplicationError.OK);
			return;
		}
		
		if ("get".compareToIgnoreCase(action)==0){
			log.info("Get Setting key = "+key);		
			if (setting!=null){
				jsonResult.put("value", setting.getValue());
				jsonResult.put("key", key);
				renderJson(response, ApplicationError.OK);
			}
			else{
				jsonResult.put("key", key);
				jsonResult.put("value", value);
				renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			}
			return;
		}
		
		if ("restore".compareToIgnoreCase(action)==0){
			log.info("Reset Setting key = "+key);		
			if (setting!=null){
				setting.setValue(setting.getDefaultValue());
				jsonResult.put("value", setting.getValue());
				jsonResult.put("key", key);
				renderJson(response, ApplicationError.OK);
			}
			else{
				jsonResult.put("key", key);
				jsonResult.put("value", value);
				renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			}
			return;
		}

		if ("update".compareToIgnoreCase(action)==0){
			log.info("Update Setting key "+key+" to value = "+value);		
			if (setting!=null){
				setting.setValue(value);
				appSettingDao.put(setting);
				jsonResult.put("value", setting.getValue());
				jsonResult.put("key", key);
				renderJson(response, ApplicationError.OK);
			}
			else{
				jsonResult.put("key", key);
				jsonResult.put("value", value);
				renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
			}
		}	
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StringUtils.trimToEmpty(request.getParameter("action"));
		
		if ("exportall".compareToIgnoreCase(action) == 0) {
			response.addHeader("Content-disposition",
					"attachment; filename=settings.json");
			List<AppSetting> settings = appSettingDao.getAll();
			renderText(response, exportJSONString(settings));
			return;
		}

		if ("reset".compareTo(action)==0){
			appSettingDao.restoreDefaultSettings();
			renderHtml(response, "/admin/settings-sucess.vm");
			return;
		}

		// if there is no parameters, just show all the settings
		List<AppSetting> settings = appSettingDao.getAll();
		context.put("settings", settings);
		renderHtml(response, "/admin/settings.vm");

	}

	private String exportJSONString(List<AppSetting> settings) {
		StringBuilder str = new StringBuilder();
		str.append("[");
		int i=0;
		int len = settings.size();
		for (AppSetting setting : settings) {
			str.append(setting.toJSON());
			i++;
			if (i<len)
				str.append(",");
		}
		str.append("]");
		return str.toString();
	}

	@Override
	protected void onProcessFileUploadSuccess(String uploadedContent) {
		log.info("Upload success.Content = "+uploadedContent);
		
		List<AppSetting> settings;
		try {
			settings = AppSettingImporter.parse(uploadedContent);
			if (settings.isEmpty()){
				onProcessFileUploadError(-5, "There is no AppSetting to import. Content = "+uploadedContent);
			}
			else{
				//import to database;
				for(AppSetting p : settings){
					appSettingDao.put(p);
				}
			}
		} catch (ParseJSONException e) {
			onProcessFileUploadError(-3, "Invalid JSON on the uploaded content.Content = "+uploadedContent);
		}
		
	}
}