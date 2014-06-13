package de.anhquan.viem.core.json;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.anhquan.viem.core.model.AppSetting;

public class AppSettingImporter {

	/**
	 * @param args
	 * @throws ParseJSONException 
	 */
	public static List<AppSetting> parse(String uploadedContent) throws ParseJSONException {
		JSONParser parser = new JSONParser();
		List<AppSetting> settings = new ArrayList<AppSetting>();
		try {
			Object obj = parser.parse(uploadedContent);
			JSONArray json = (JSONArray) obj;
			for(int i=0;i<json.size();i++){
				JSONObject item = (JSONObject) json.get(i);
				AppSetting p = new AppSetting();
				p.fromJSON(item);
				settings.add(p);
			}
		} catch (ParseException e) {
			throw new ParseJSONException("Invalid JSON on the uploaded content. Content = "+uploadedContent, e);
		} catch (ClassCastException e){
			throw new ParseJSONException("Uploaded content must contain an array of AppSettings. Content = "+uploadedContent, e);
		}
		
		return settings;
	}

}
