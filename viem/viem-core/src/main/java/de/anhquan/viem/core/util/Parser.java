package de.anhquan.viem.core.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class Parser {

	public static boolean parseBoolean(Object obj){
		boolean value = false;
		if (obj==null)
			return value;
		
		if (obj instanceof Boolean)
			return ((Boolean)obj).booleanValue();
		
		String input = StringUtils.trimToEmpty((String)obj);
		if (!input.isEmpty()){
			try{
				value = (("1".compareToIgnoreCase(input)==0) || ("true".compareToIgnoreCase(input)==0) || ("on".compareToIgnoreCase(input)==0)  || ("checked".compareToIgnoreCase(input)==0)) ? true : false;
			}
			catch (Exception e) {}
		}
		return value;
	}
	
	public static Long parseLong(Object input){
		if (input == null)
			return new Long(0);
		
		if (input instanceof Long)
			return (Long)input;
		
		Long value = new Long(0);
		String s = StringUtils.trimToEmpty((String)input);
		if (!s.isEmpty()){
			try{
				value = Long.parseLong(s);
			}
			catch (Exception e) {}
		}
		return value;
	}
	
	public static int parseInt(Object obj){
		if (obj == null)
			return 0;
		
		if (obj instanceof String){
			String s = StringUtils.trimToEmpty((String)obj);
			int p = 0;
			try{
				p = Integer.parseInt(s);
			}
			catch (Exception e){}
			return p;
		}
		else if (obj instanceof Number){
			return ((Number)obj).intValue();
		}
		
		return 0;
	}

	public static String parseString(Object obj) {
		if (obj == null)
			return "";
		
		if (obj instanceof String)
			return StringUtils.trimToEmpty((String)obj);

		return obj.toString(); 
	}

	
	//TODO: refactor
	public static Date parseDate(Object obj) {
		if (obj == null)
			return null;
		
		if (obj instanceof Date)
			return (Date)obj;

		return null; 
	}

	public static Double parseDouble(String obj) {
		if (obj == null)
			return new Double(0);
		
		if (obj instanceof String){
			String s = StringUtils.trimToEmpty((String)obj);
			Double p = new Double(0);
			try{
				p = Double.parseDouble(s);
			}
			catch (Exception e){}
			return p;
		}
		
		return new Double(0);
	}
	
	
}
