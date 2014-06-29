package de.anhquan.viem.core.model;

import de.anhquan.config4j.Config;
import de.anhquan.config4j.annotation.ConfigContainer;
import de.anhquan.config4j.annotation.ConfigParam;

@ConfigContainer(Prefix = "app.")
public interface AppConfig extends Config {

	@ConfigParam(DefaultString="1.0.0", Title = "Version", Description = "Application version")
	public String getVersion();
	public void setVersion(String version);
	
	@ConfigParam(DefaultBoolean=true, Title = "Openning", Description = "Is store openned")
	public boolean getOpenning();
	public void setOpenning(boolean openning);
	
	@ConfigParam(DefaultString="Copyright (C) Demoshop.com", Title = "Footer 1", Description = "Footer line 1")
	public String getFooterLine1();
	public void setFooterLine1(String value);
	
	@ConfigParam(DefaultString="Powered by Google Appengine", Title = "Footer 2", Description = "Footer line 2")
	public String getFooterLine2();
	public void setFooterLine2(String value);
	
	@ConfigParam(DefaultString="/base/images/logo.jpg", Title = "Logo", Description = "Store logo")
	public String getLogo();
	public void setLogo(String value);
	
	@ConfigParam(DefaultInteger=2, Title = "Store Status", Description = "Store Status")
	public int getShopStatus();
	public void setShopStatus(int status);
	
	@ConfigParam(DefaultString="thaiexpress", Title = "Theme", Description = "The current front-end theme")
	public String getTheme();
	public void setTheme(String themeName);
		
	@ConfigParam(DefaultString="0123456789", Title = "Telephone", Description = "Your store phone number")
	public String getShopTelephone();
	public void setShopTelephone(String phoneNr);
	
	@ConfigParam(DefaultString="012345679", Title = "Fax", Description = "Your store fax number")
	public String getShopFax();
	public void setShopFax(String faxNr);
	
	@ConfigParam(DefaultString="012345679", Title = "Email", Description = "Your Email store")
	public String getShopEmail();
	public void setShopEmail(String email);
	
	@ConfigParam(DefaultString="de-DE", Title = "Locale", Description = "Locale in Frontend")
	public String getLocale();
	public void setLocale(String str);
	
	@ConfigParam(DefaultString="", Title = "Common Page Meta", Description = "Page Meta for SEO")
	public String getCommonPageMeta();
	public void setCommonPageMeta(String meta);
	
	@ConfigParam(DefaultString="/base/images/default-product-image.png", Title = "Default Product Image", Description = "Used when the product has no image")
	public String getDefaultProductImage();
	public void setDefaultProductImage(String img);
	
	@ConfigParam(DefaultString="/base/images/default-product-thumbnail.png", Title = "Default Product Thumbnail", Description = "Used when the product has no image")
	public String getDefaultProductThumbnail();
	public void setDefaultProductThumbnail(String img);
	
	@ConfigParam(DefaultString="", Title = "Sidebar Link List", Description = "Showed in sidebar")
	public String getSidebarLinkList();
	public void setSidebarLinkList(String linkList);
	
	@ConfigParam(DefaultString="/index.html;Startseite|/category/vorspeisen;Speisekarte|/partyservice.html;Party Service|/kontakt.html;Kontakt|/impressum.html;Impressum", Title = "Top Nav", Description = "")
	public String getTopNav();
	public void setTopNav(String nav);
	
	@ConfigParam(DefaultString="", Title = "Openning Time", Description = "")
	public String getOpeningTime();
	public void setOpeningTime(String timeDuration);

	@ConfigParam(DefaultString="https://plus.google.com/101678113405092816528", Title = "SEO Publisher", Description = "https://plus.google.com/YOUR_PLUS_ID")
	public String getPublisher();
	public void setPublisher(String publisher);
	
	@ConfigParam(DefaultString="50.765670, 6.088957", Title = "Geo Position", Description = "Geo Position")
	public String getGeoPosition();
	public void setGeoPosition(String position);
	
	@ConfigParam(DefaultString="NRW", Title = "Geo Region", Description = "Geo Region")
	public String getGeoRegion();
	public void setGeoRegion(String region);
	
	@ConfigParam(DefaultString="/base/images/favicon.ico", Title = "Favicon", Description = "16x16 pixel")
	public String getFavIcon();
	public void setFavIcon(String iconPath);

	@ConfigParam(DefaultString="Dai Duong Restaurant", Title = "Shop Name", Description = "og:site_name")
	public String getShopName();
	public void setShopName(String name);

	@ConfigParam(DefaultString="http://daiduong-restaurant.de", Title = "Site URL", Description = "Does not end with /")
	public String getSiteUrl();
	public void setSiteUrl(String url);

	@ConfigParam(DefaultString="", Title = "Google Analytics", Description = "UA-code ...")
	public String getGoogleAnalytics();
	public void setGoogleAnalytics(String value);
	
	@ConfigParam(DefaultString="/base/images/logo.png", Title = "Facebook Image", Description = "og:image")
	public String getFacebookImage();
	public void setFacebookImage(String imagePath);
	
	@ConfigParam(DefaultString="Burtscheider Str.", Title = "Shop Street", Description = "e.g. Hauptstr.")
	public String getShopStreet();
	public void setShopStreet(String str);
	
	@ConfigParam(DefaultString="27", Title = "Shop House Number", Description = "e.g. 15")
	public String getShopHouseNumber();
	public void setShopHouseNumber(String str);

	@ConfigParam(DefaultString="52064", Title = "Shop Zipcode", Description = "e.g. 52064")
	public String getShopZipcode();
	public void setShopZipcode(String str);
	
	@ConfigParam(DefaultString="Aachen", Title = "Shop City", Description = "e.g. Aachen")
	public String getShopCity();
	public void setShopCity(String str);

	@ConfigParam(DefaultString="NRW", Title = "Shop State", Description = "e.g. NRW")
	public String getShopState();
	public void setShopState(String str);

	@ConfigParam(DefaultString="Germany", Title = "Shop Country", Description = "e.g. Germany")
	public String getShopCountry();
	public void setShopCountry(String str);
	
	@ConfigParam(DefaultString="", Title = "robots.txt", Description = "Content of the file robots.txt")
	public String getRobotsTxt();
	public void setRobotsTxt(String str);

}
