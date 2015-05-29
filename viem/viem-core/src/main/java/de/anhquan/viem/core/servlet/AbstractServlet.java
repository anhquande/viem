package de.anhquan.viem.core.servlet;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.json.simple.JSONObject;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;

import de.anhquan.config4j.ConfigFactory;
import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.Constants;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.model.AppConfig;
import de.anhquan.viem.core.model.AppUser;
import de.anhquan.viem.core.model.EntityNotFoundException;
import de.anhquan.viem.core.model.InvalidParameterException;
import de.anhquan.viem.core.model.NavigationItem;
import de.anhquan.viem.core.util.Formatter;
import de.anhquan.viem.core.util.Parser;
import de.anhquan.viem.core.velocity.VelocityEngineManager;

public abstract class AbstractServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected static final String ENCODING_UTF_8 = "UTF-8";

	protected static final String APPLICATION_JSON = "application/json";

	protected static final String TEXT_HTML = "text/html";

	protected VelocityContext context;
	
	private static final Logger log = Logger
			.getLogger(AbstractServlet.class.getName());

	protected static final CharSequence MULTIPART_FORM_DATA = "multipart/form-data";
	
	protected String currentTheme;
	
	protected String servletBasePath;
	
	protected final JSONObject jsonResult;
	
	protected final AppConfig config;


	@Inject
	public AbstractServlet() {
		super();
		if (this.getClass().isAnnotationPresent(ServletPath.class)){
			ServletPath ann = this.getClass().getAnnotation(ServletPath.class);
			this.servletBasePath = ann.value();
		}
		
		config = ConfigFactory.getConfig(AppConfig.class);
		this.jsonResult = new JSONObject();
		context = new VelocityContext();
	}

	public void rereadSettings(){
		log.info("reread settings ...");
		currentTheme = "/themes/"+config.getTheme();
		context.put("theme", currentTheme);
		context.put("footer1", config.getFooterLine1());
		context.put("footer2", config.getFooterLine2());
		context.put("shopStatus", config.getShopStatus());
		context.put("shopOpeningTime", config.getOpeningTime());

		context.put("StringUtils", new StringUtils());
		context.put("Formatter", Formatter.INSTANCE);
		Formatter.LOCALE = config.getLocale();
		
		context.put("logo", config.getLogo());
		context.put("pageMeta", config.getCommonPageMeta());
		context.put("topnav", NavigationItem.createList(config.getTopNav()));
		context.put("sidebarLinkList", NavigationItem.createList(config.getSidebarLinkList()));
		context.put("defaultProductThumbnail",  config.getDefaultProductThumbnail());
		context.put("defaultProductImage",  config.getDefaultProductImage());
		
		context.put("shopStreet", config.getShopStreet());
		context.put("shopHouseNumber", config.getShopHouseNumber());
		context.put("shopZipcode", config.getShopZipcode());
		context.put("shopCity", config.getShopCity());
		context.put("shopState", config.getShopState());
		context.put("shopCountry", config.getShopCountry());
		context.put("shopFax", config.getShopFax());
		context.put("shopTelephone", config.getShopTelephone());
		context.put("shopEmail", config.getShopEmail());

		context.put("facebookImage", config.getFacebookImage());
		context.put("favIcon", config.getFavIcon());
		context.put("geoPosition", config.getGeoPosition());
		context.put("geoRegion", config.getGeoRegion());
		context.put("publisher", config.getPublisher());
		context.put("shopName", config.getShopName());
		context.put("siteUrl", config.getSiteUrl());
		context.put("googleAnalytics", config.getGoogleAnalytics());

	}
	
	protected abstract void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, EntityNotFoundException, InvalidParameterException;
	
	protected abstract void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, EntityNotFoundException, InvalidParameterException;
		
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			log.info("abstract.doPost ...");
			processRequest(request, response);
		} catch (EntityNotFoundException e) {
			log.warning("Error when doPost. Errmsg = "+e.getMessage());
			renderJson(response, ApplicationError.ENTITY_NOT_FOUND);
		} catch (InvalidParameterException e) {
			log.severe("Error when doPost. Errmsg = "+e.getMessage());
			jsonResult.put("reason", e.getMessage());
			renderJson(response, ApplicationError.INVALID_PARAMETER);
		}
	}

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException, EntityNotFoundException, InvalidParameterException {
		
		log.info("abstract.processResquest ...");
		AppUser user = (AppUser) request.getSession().getAttribute(Constants.SESSION_ADMIN_USER);
		context.put("user", user);
		rereadSettings();

		// check if this is the file upload request
		String contentType = request.getContentType();
		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {
			processFileUpload(request, response);
			return;
		}

		//from now on process as normal 
		response.setCharacterEncoding(ENCODING_UTF_8);
		String accept = request.getHeader("Accept");
		log.info("Accept = "+accept);
		if ((accept!=null) && (accept.contains(APPLICATION_JSON))){
			response.setContentType(APPLICATION_JSON);
			processJsonRequest(request, response);
		}
		else if (accept.contains("application/x-www-form-urlencoded")){
			processFileUpload(request, response);
		}
		else{
			response.setContentType(TEXT_HTML);
			processHtmlRequest(request, response);
		}
		
	}
	
	protected void processFileUpload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		log.info("Process File Upload ...");
		DataInputStream in = new DataInputStream(request.getInputStream());
		int formDataLength = request.getContentLength();
		byte dataBytes[] = new byte[formDataLength];
		int byteRead = 0;
		int totalBytesRead = 0;
		while (totalBytesRead < formDataLength) {
			byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
			totalBytesRead += byteRead;
		}

		String body = new String(dataBytes, "UTF-8");

		String contentType = request.getContentType();
		int i = contentType.indexOf("boundary=");
		if (i <= 0) {
			log.info("Invalid file upload request");
			onProcessFileUploadError(-1,"Invalid Request");
		}

		String boundary = contentType.substring(i + "boundary=".length());
		BufferedReader reader = new BufferedReader(new StringReader(body));
		String line;
		StringBuilder content = new StringBuilder();
		boolean foundContent = false;
		while ((line = reader.readLine()) != null) {
			if (!foundContent){
				if (line.isEmpty()){
					foundContent = true;
					continue;
				}
			}
			
			if (foundContent){
				if (line.contains(boundary))
					break;
				content.append(line);
			}
		}

		if (foundContent)
			onProcessFileUploadSuccess(content.toString());
		else
			onProcessFileUploadError(-2,"Uploaded content not found");
	}

	protected void onProcessFileUploadSuccess(String uploadedContent) {
	}

	protected void onProcessFileUploadError(int errno, String errmsg) {		
		log.warning(errmsg);
	}

	@SuppressWarnings("unchecked")
	protected final void renderJson(HttpServletResponse response, ApplicationError error) throws ServletException, IOException{
		PrintWriter writer;
		writer = response.getWriter();
		jsonResult.put("errno", error.getErrorNumber());
		jsonResult.put("errmsg", error.getErrorMessage());
		writer.write(jsonResult.toJSONString());
		writer.flush();
		writer.close();
	}
	
	protected final void renderHtml (HttpServletResponse response, String page) throws ServletException, IOException{
		Template template = VelocityEngineManager.getTemplate(page);
		
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer;
		writer = response.getWriter();
		template.merge(context, writer);
		writer.flush();
		writer.close();
	}

	protected final void renderText (HttpServletResponse response, String content) throws ServletException, IOException{
		
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter writer;
		writer = response.getWriter();
		writer.write(content);
		writer.flush();
		writer.close();
	}
	
	protected void replyPageNotFound(HttpServletResponse response) throws ServletException, IOException {
		renderHtml(response, currentTheme+"/pages/404.vm");
	}

	public String getServletBasePath(){
		return this.servletBasePath;
	}
	
	/**
	 * Read ID from request param
	 * @param request
	 * @return 0 if the ID is not specified, otherwise the ID number 
	 */
	public Long readId(HttpServletRequest request) {
		return Parser.parseLong(request.getParameter("id"));
	}
}