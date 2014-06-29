package de.anhquan.viem.core.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.anhquan.viem.core.annotation.ServletPath;

@ServletPath(value="/login_admin")
public class AdminLoginServlet extends AbstractServlet {

	private static final long serialVersionUID = -5451465544422878195L;

	@Override
	protected void processJsonRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}

	@Override
	protected void processHtmlRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {		
		UserService service = UserServiceFactory.getUserService();
		String loginURL = service.createLoginURL("/admin/page");
		context.put("loginURL", loginURL);
		renderHtml(response, "/admin/login.vm");		
	}

	
}