package de.anhquan.viem.core.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Formatter {

	public static Formatter INSTANCE = new Formatter();
	
	public static String LOCALE = "de-DE";
	
	protected Formatter(){
		
	}
	
	public static String formatCurrency(int cent){
		return formatCurrency(cent,",");
	}
	
	public static String formatCurrency(long cent, String separator){
		Locale currentLocale = Locale.forLanguageTag(LOCALE);
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(currentLocale);
		String pattern = "###,###.00";
		DecimalFormat myFormatter = new DecimalFormat(pattern, symbols);
		String output = myFormatter.format(cent/100.0);
		return output;
	}
}
