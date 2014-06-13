package de.anhquan.viem.core.dao;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.ObjectifyService;

import de.anhquan.viem.core.model.AppSetting;

public class AppSettingDao extends NameBasedDao<AppSetting> {

    static {
        ObjectifyService.register(AppSetting.class);
    }

	public static final Logger log = Logger.getLogger(AppSettingDao.class.getName());
	
	protected AppSettingDao() {
		super(AppSetting.class);
	}

	public AppSetting findByKey(String name){
		return findFirstItemWithName(name);
	}
	
	public String getSettingValue(String name){
		AppSetting setting = findFirstItemWithName(name);
		if (setting==null)
			return "";
		else
			return setting.getValue();
	}
		
	private void addDefaultSetting(int sortValue, String group, String key, String title, String value){
		log.info("add setting '"+key+"' = "+value);
		AppSetting setting = give(key);
		setting.setGroup(group);
		setting.setValue(value);
		setting.setDefaultValue(value);
		setting.setTitle(title);
		
		put(setting);
	}
	
	public void createDefaultSettings() {
		log.info("Create default Settings...");

		addDefaultSetting(1, AppSetting.GROUP_APPLICATION, AppSetting.APP_TITLE, "Application Title", "Demo Shop");
		addDefaultSetting(2, AppSetting.GROUP_APPLICATION, AppSetting.APP_VERSION, "Application Version", "");
		addDefaultSetting(3, AppSetting.GROUP_APPLICATION, AppSetting.APP_THEME, "Application Theme","thaiexpress");
		addDefaultSetting(4, AppSetting.GROUP_APPLICATION, AppSetting.APP_FOOTER1, "Footer Line 1 ", "&copy; 2014 Demo Shop. Irrtum und Änderungen vorbehalten. <a href=\"http://demoshop.com/pages/impressum\" target=\"_blank\">Impressum</a>");
		addDefaultSetting(5, AppSetting.GROUP_APPLICATION, AppSetting.APP_FOOTER2, "Footer Line 2 ", "Powered by <a href=\"http://appengine.google.com\">Google App Engine</a>, <a href=\"http://getbootstrap.com/\" target=\"_blank\">Bootstrap 3</a>, <a href=\"http://glyphicons.com/\">Glyphicons Free</a>. Entwickelt von <a href=\"http://anhquan.de\">Anh Quan Nguyen</a>");
		addDefaultSetting(6, AppSetting.GROUP_APPLICATION, AppSetting.APP_TOPNAV, "Top Navigation", "/index.html;Startseite|/category.html;Speisekarte|/partyservice.html;Partyservice|/kontakt.html;Kontakt|/impressum.html;Impressum");
		addDefaultSetting(7, AppSetting.GROUP_APPLICATION, AppSetting.APP_SIDEBAR_LINKLIST, "Sidebar Link List", "/category;Speisekarte|/category/vorspeisen;Vorspeisen|/category/huehnerfleisch;Hühnerfleisch|/category/huehnerbrust;Panierter Hühnerbrust|/category/knuspriges-haehnchen;Knuspriges Hähnchen|/category/knusprige-ente;Knusprige Ente|/category/rindfleisch;Rindfleisch|/category/fisch;Fisch|/category/hummerkrabben;Hummerkrabben|/category/spezialitaeten;Spezialitäten|/category/vegetarisch;Vegetarisch|/category/kleine-portionen;Kleine Portionen|/category/beilagen;Beilagen|/category/desserts;Desserts");
		addDefaultSetting(8, AppSetting.GROUP_APPLICATION, AppSetting.APP_SIDEBAR, "Sidebar Content", "Change side bar in admin/settings");
		addDefaultSetting(9, AppSetting.GROUP_APPLICATION, AppSetting.APP_PAGE_META, "Page Metadata", "<meta name=\"geo.placename\" content=\"Burtscheider Straße 27, 52064 Aachen, Germany\" />");
		addDefaultSetting(10, AppSetting.GROUP_APPLICATION, AppSetting.APP_LOGO, "Logo", "/themes/thaiexpress/images/logo.png");

		addDefaultSetting(20, AppSetting.GROUP_STORE, AppSetting.STORE_NAME, "Store Name", "Demo Shop");
		addDefaultSetting(21, AppSetting.GROUP_STORE, AppSetting.STORE_ADDRESS_LINE1, "Address Line 1", "Demostr 1");
		addDefaultSetting(22, AppSetting.GROUP_STORE, AppSetting.STORE_ADDRESS_LINE2, "Address Line 2", "12345 Democity");		
		addDefaultSetting(23, AppSetting.GROUP_STORE, AppSetting.STORE_TELEPHONE, "Store Telephone", "0123 456 789 10");
		addDefaultSetting(24, AppSetting.GROUP_STORE, AppSetting.STORE_FAX, "Store Fax", "0123 456 789 11");
		addDefaultSetting(25, AppSetting.GROUP_STORE, AppSetting.STORE_WEBSITE, "Store Website", "http://demoshop.com.com");
		addDefaultSetting(25, AppSetting.GROUP_STORE, AppSetting.STORE_EMAIL, "Store Email", "info@demo.com");		
		addDefaultSetting(27, AppSetting.GROUP_STORE, AppSetting.STORE_OPENING_TIME, "Opening Time","Mo. - Fr.: 11:00 - 23:00");
		addDefaultSetting(30, AppSetting.GROUP_STORE, AppSetting.STORE_MODE, "Store mode (0=normal,1=busy,2=closed,3=problem)","0");
		addDefaultSetting(31, AppSetting.GROUP_STORE, AppSetting.STORE_TIMEZONE, "Timezone","Europe/Berlin");
		addDefaultSetting(32, AppSetting.GROUP_STORE, AppSetting.STORE_LOCALE, "Locale","de-DE");

		addDefaultSetting(40, AppSetting.GROUP_BASIC, AppSetting.REFRESH_INTERVAL, "Frontend Refresh Interval (in msec)","60000");
		addDefaultSetting(41, AppSetting.GROUP_BASIC, AppSetting.REFRESH_CONTROL_INTERVAL, "Control Board Refresh Interval (in msec)","30000");
			
		addDefaultSetting(107, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_0_0, "Text: Title for Status 0 in Normal Mode", "Bestellung");
		addDefaultSetting(108, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_0_0, "Text: Status Description 0 (Normal)", "Vielen Dank, wir haben Ihre Bestellung erhalten!");
		addDefaultSetting(109, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_0_1, "Text: Title for Status 0 in Busy Mode", "Bestellung");
		addDefaultSetting(110, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_0_1, "Status Description 0 (Busy)", "Vielen Dank, wir haben Ihre Bestellung erhalten! Aufgrund der hohen Auftragsvolumen gibt es eine Verzögerung bei der Bearbeitung von Aufträgen im Moment. Um frische und warme Speisen an Sie zu liefern, wir kochen es nur richtig, vor der Auslieferung. Im Moment kann es bis zu 20 Minuten dauern, bis das weitere Verfahren. Vielen Dank für Ihre Patienten.");

		addDefaultSetting(111, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_1_0, "Text: Title for Status 1 in Normal Mode", "Zubereitung");
		addDefaultSetting(112, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_1_0, "Text: Status Description 1 (Normal)","Ihre Bestellung wird gerade für Sie zubereitet! Wir verwenden stets frische Zutaten.");
		addDefaultSetting(113, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_1_1, "Text: Title for Status 1 in Busy Mode", "Zubereitung");
		addDefaultSetting(114, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_1_1, "Text: Status Description 1 (Busy)","Ihre Bestellung wird gerade für Sie zubereitet! Wir verwenden stets frische Zutaten.");

		addDefaultSetting(115, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_2_0, "Text: Title for Status 2 in Normal Mode", "Lieferung");
		addDefaultSetting(116, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_2_0, "Text: Status Description 2 (Normal)","Ihre Bestellung ist nun auf dem Weg zu Ihnen. Die momentane Lieferzeit im Umkreis von 5 km beträgt zwischen 15 und 20 Minuten.");		
		addDefaultSetting(117, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_2_1, "Text: Title for Status 2 in Busy Mode", "Lieferung");
		addDefaultSetting(118, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_2_1, "Text: Status Description 2 (Busy)","Ihre Bestellung ist nun auf dem Weg zu Ihnen. Aufgrund hohen Andrangs kann es heute bei der Auslieferung zu Verzögerungen kommen. Wir bitten um Ihr Verständnis.");		
		
		addDefaultSetting(119, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_3_0, "Text: Title for Status 3 in Normal Mode", "Fertig");
		addDefaultSetting(120, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_3_0, "Text: Status Description 3 (Normal)","Wir wünschen Ihnen einen guten Appetit. Ihre Meinung ist uns wichtig! Bewerten Sie uns gerne auf unserer Homepage.");
		addDefaultSetting(121, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_TITLE_3_1, "Text: Title for Status 0 in Busy Mode", "Fertig");
		addDefaultSetting(122, AppSetting.GROUP_TEXT, AppSetting.TEXT_STATUS_DESCRIPTION_3_1, "Text: Status Description 3 (Busy)","Wir wünschen Ihnen einen guten Appetit. Ihre Meinung ist uns wichtig! Bewerten Sie uns gerne auf unserer Homepage.");

		addDefaultSetting(130, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_TITLE_0, "Text: Normal Store Mode", "Normal");
		addDefaultSetting(132, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_TITLE_1, "Text: Busy Store Mode", "Busy");
		addDefaultSetting(134, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_TITLE_2, "Text: Closed Store Mode", "Geschlossen");
		addDefaultSetting(136, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_TITLE_3, "Text: Problem Store Mode", "Ausserbetrieb");

		addDefaultSetting(131, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_DESCRIPTION_0, "Text: Store Mode Description (Normal)", "Wir sind geoffnet.");
		addDefaultSetting(133, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_DESCRIPTION_1, "Text: Store Mode Description (Busy)", "Wir sind momentan sehr beschäftigt.");
		addDefaultSetting(135, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_DESCRIPTION_2, "Text: Store Mode Description (Closed)", "Unsere Shop ist momentan geschlossen.");
		addDefaultSetting(137, AppSetting.GROUP_TEXT, AppSetting.STORE_MODE_DESCRIPTION_3, "Text: Store Mode Description (Problem)", "Wir stießen auf ein technisches Problem und muss das Tracking-System zu schließen. Wir entschuldigen uns für die Unannehmlichkeiten.");

		addDefaultSetting(200, AppSetting.GROUP_PRODUCT, AppSetting.PRODUCT_DEFAULT_IMAGE, "Default Product Image","/themes/thaiexpress/images/default-product-image.png");
		addDefaultSetting(201, AppSetting.GROUP_PRODUCT, AppSetting.PRODUCT_DEFAULT_THUMBNAIL, "Default Product Thumbnail","/themes/thaiexpress/images/default-product-thumbnail.png");
		
	}
	
//	public String now(){
//		return timeAsString(new Date());
//	}
//	
//	public String timeAsString(Date date){
//		if (date==null)
//			return null;
//		SimpleDateFormat df = new SimpleDateFormat("dd.MMM HH:mm");
//		String timezone = getSettingValue(AppSetting.STORE_TIMEZONE);
//		if ((timezone==null) || (timezone.isEmpty()))
//			timezone = "Europe/Berlin";
//		df.setTimeZone(TimeZone.getTimeZone(timezone));
//		
//		return df.format(date);
//	}

	public void restoreDefaultSettings() {
		List<AppSetting> settings = getAll();
		for (AppSetting setting : settings) {
			setting.restoreDefault();
			put(setting);
		}
	}

}