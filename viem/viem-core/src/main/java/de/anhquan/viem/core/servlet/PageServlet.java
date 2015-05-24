package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.PageDao;
import de.anhquan.viem.core.model.Page;

@ServletPath(value="*.html")
public class PageServlet extends AbstractServlet {

	private PageDao pageDao;
	
	public static final Logger log = Logger.getLogger(PageServlet.class
			.getName());

	private static final long serialVersionUID = -6630427559567895991L;
	
	@Inject
	public PageServlet(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
	}

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pageName = StringUtils.trimToEmpty(request.getRequestURI());

		pageName = pageName.substring(1, pageName.length() - ".html".length());

		Page page = pageDao.findFirstItemWithName(pageName);
		if (page==null){
			renderHtml(response, currentTheme+"/pages/404.vm");
			return;
		}
		
		context.put("page", page);
		
		context.put("url", config.getSiteUrl()+"/"+page.getName()+".html");
		
		String template = "default";
		if (!StringUtils.isEmpty(page.getTemplate())){
			template = page.getTemplate();
		}
		
		renderHtml(response, currentTheme+"/pages/page."+template+".vm");
	}
}