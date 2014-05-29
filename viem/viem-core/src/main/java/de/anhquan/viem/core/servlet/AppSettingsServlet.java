package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.model.AppSetting;
import de.anhquan.viem.core.util.AppSettingImporter;
import de.anhquan.viem.core.util.ParseSettingException;

public class AppSettingsServlet extends AbstractServlet {

	private static final long serialVersionUID = 8191489816403471099L;
	public static final Logger log = Logger.getLogger(AppSettingsServlet.class.getName());
	
	AppSettingDao settingDao;
	
	@Inject
	public AppSettingsServlet(
			AppSettingManager settingManager, AppSettingDao settingDao){
		super(settingManager);
		this.settingDao = settingDao;
	}

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String action = StringUtils.trimToEmpty(request.getParameter("action"));		
		String key = StringUtils.trimToEmpty(request.getParameter("key"));
		String value = StringUtils.trimToEmpty(request.getParameter("value"));
		AppSetting setting = settingManager.findByKey(key);
		JSONObject 	json = new JSONObject();

		if ("clearall".compareToIgnoreCase(action)==0){
			List<AppSetting> settings = settingDao.getAll();
			settingDao.delete(settings);
			settingManager.clearCache();
			replyOk(response, null);
			return;
		}

		if ("saveall".compareToIgnoreCase(action)==0){
			settingManager.writeToDataStore();
			replyOk(response,null);
			return;
		}
		
		if ("loadall".compareToIgnoreCase(action)==0){
			settingManager.readFromDataStore();
			replyOk(response,null);
			return;
		}
		
		if ("get".compareToIgnoreCase(action)==0){
			log.info("Get Setting key = "+key);		
			if (setting!=null){
				json.put("errno", "0");
				json.put("errmsg", "Success");
				if (AppSetting.STORE_NOW.compareTo(key)==0)
					json.put("value", settingManager.now());
				else
					json.put("value", setting.getValue());
				json.put("key", key);
			}
			else{
				json.put("errno", "1");
				json.put("errmsg", "Setting Key ("+key+") not found");
				json.put("key", key);
				json.put("value", value);
			}
			renderJson(response,json);
			return;
		}
		
		if ("restore".compareToIgnoreCase(action)==0){
			log.info("Reset Setting key = "+key);		
			if (setting!=null){
				setting.setValue(setting.getDefaultValue());
				json.put("errno", "0");
				json.put("errmsg", "Success");
				json.put("value", setting.getValue());
				json.put("key", key);
			}
			else{
				json.put("errno", "1");
				json.put("errmsg", "Setting Key ("+key+") not found");
				json.put("key", key);
				json.put("value", value);
			}
			renderJson(response,json);
			return;
		}

		if ("update".compareToIgnoreCase(action)==0){
			log.info("Update Setting key "+key+" to value = "+value);		
			if (setting!=null){
				setting.setValue(value);
				settingManager.put(setting);
				json.put("errno", "0");
				json.put("errmsg", "Success");
				json.put("key", key);
				json.put("value", value);
			}
			else{
				json.put("errno", "1");
				json.put("errmsg", "Setting Key ("+key+") not found");
				json.put("key", key);
				json.put("value", value);
			}
			renderJson(response,json);
		}	
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StringUtils.trimToEmpty(request.getParameter("action"));
		
		if ("exportall".compareToIgnoreCase(action) == 0) {
			settingManager.writeToDataStore();
			response.addHeader("Content-disposition",
					"attachment; filename=settings.json");
			List<AppSetting> settings = settingDao.getAll();
			renderText(response, exportJSONString(settings));
			return;
		}

		if ("reset".compareTo(action)==0){
			settingManager.resetSettings();
			renderHtml(response, "/admin/settings-sucess.vm");	
		}
		else {
			List<AppSetting> settings = settingManager.getAll();
			context.put("settings", settings);
			renderHtml(response, "/admin/settings.vm");
		}

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

	private void replyOk(HttpServletResponse response, Map data)
			throws ServletException, IOException {
		JSONObject json = new JSONObject();
		if (data != null)
			json.putAll(data);

		json.put("errno", 0);
		json.put("errmsg", "OK");

		renderJson(response, json);
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
					settingDao.put(p);
				}
			}
		} catch (ParseSettingException e) {
			onProcessFileUploadError(-3, "Invalid JSON on the uploaded content.Content = "+uploadedContent);
		}
		
	}
}