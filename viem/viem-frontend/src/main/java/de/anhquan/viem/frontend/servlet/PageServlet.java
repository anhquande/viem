package de.anhquan.viem.frontend.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.servlet.AbstractServlet;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.dao.cache.PageManager;
import de.anhquan.viem.frontend.model.Page;

@ServletPath(basePath="*.html")
public class PageServlet extends AbstractServlet {

	private PageManager pageManager;
	
	public static final Logger log = Logger.getLogger(PageServlet.class
			.getName());

	
	@Inject
	public PageServlet(AppSettingManager settingManager, PageManager pageManager) {
		super(settingManager);
		this.pageManager = pageManager; 
	}

	private static final long serialVersionUID = -6630427559567895991L;

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pageName = StringUtils.trimToEmpty(request.getRequestURI());

		pageName = pageName.substring(1, pageName.length() - ".html".length());

		Page page = pageManager.findByName(pageName);
		if (page==null){
			renderHtml(response, currentTheme+"/pages/404.vm");
			return;
		}
		
		context.put("page", page);
		
		String template = "default";
		if (!StringUtils.isEmpty(page.getTemplate())){
			template = page.getTemplate();
		}
		
		renderHtml(response, currentTheme+"/pages/"+template+".vm");
	}
}