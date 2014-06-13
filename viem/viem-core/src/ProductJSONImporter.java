package de.anhquan.viem.core.json;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.inject.Inject;

import de.anhquan.viem.core.model.BaseEntity;

public class ProductJSONImporter<T extends BaseEntity> implements JSONImporter<T>{
	public static final Logger log = Logger.getLogger(ProductJSONImporter.class
			.getName());

    protected final Class<T> clazz;

	@Inject
	protected ProductJSONImporter(final Class<T> clazz){
		this.clazz = clazz;
	}
	
	@Override
	public List<T> parse(String strJSON) throws ParseJSONException{
		JSONParser parser = new JSONParser();
		List<T> entities = new LinkedList<T>();
		try {
			Object obj = parser.parse(strJSON);
			JSONArray json = (JSONArray) obj;
			
			for(int i=0;i<json.size();i++){
				JSONObject item = (JSONObject) json.get(i);
				T p;
				try {
					p = clazz.newInstance();
					p.fromJSON(item);
					entities.add(p);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		} catch (ParseException e) {
			throw new ParseJSONException("Invalid JSON String", e);
		} catch (ClassCastException e){
			throw new ParseJSONException("Invalid JSON String",e);
		}
		
		return entities;
	}
}
