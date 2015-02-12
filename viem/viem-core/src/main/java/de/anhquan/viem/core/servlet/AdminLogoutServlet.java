package de.anhquan.viem.core.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import de.anhquan.viem.core.Constants;
import de.anhquan.viem.core.annotation.ServletPath;

@ServletPath(value="/logout_admin")
public class AdminLogoutServlet extends HttpServlet{

	private static final long serialVersionUID = 8926597217367500528L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doLogout(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doLogout(req, resp);
	}
	
	protected void doLogout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getSession().setAttribute(Constants.SESSION_ADMIN_USER, null);
		UserService service = UserServiceFactory.getUserService();
		String logoutURL = service.createLogoutURL("/login_admin");
		RequestDispatcher rd = req.getRequestDispatcher(logoutURL);
		rd.forward(req, resp);

	}
}
