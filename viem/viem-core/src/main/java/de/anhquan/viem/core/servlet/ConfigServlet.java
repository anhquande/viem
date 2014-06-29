package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.ConfigDao;
import de.anhquan.viem.core.model.Action;

@ServletPath(value="/api/settings")
public class ConfigServlet extends AbstractServlet {

	private final ConfigDao configDao;
	
	public static final Logger log = Logger.getLogger(ConfigServlet.class
			.getName());

	private static final long serialVersionUID = -6630427559567895991L;
	
	@Inject
	public ConfigServlet(ConfigDao configDao) {
		this.configDao = configDao;
	}

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Action action = Action.parse(request.getParameter("action"));
		String key = StringUtils.trimToEmpty(request.getParameter("key"));
		switch(action){
			case GET_KEY:
				
			case SET_KEY:
		}
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Action action = Action.parse(request.getParameter("action"));

		
		
		renderHtml(response, currentTheme+"/pages/setting.vm");
	}
}