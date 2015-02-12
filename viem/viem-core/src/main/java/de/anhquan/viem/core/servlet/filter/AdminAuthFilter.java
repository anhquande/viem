package de.anhquan.viem.core.servlet.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.inject.Inject;
import com.restfb.util.StringUtils;

import de.anhquan.viem.core.Constants;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppUserDao;
import de.anhquan.viem.core.model.AppUser;

@ServletPath("/admin*")
public class AdminAuthFilter implements Filter{

	private static final Logger log = Logger
			.getLogger(AdminAuthFilter.class.getName());

	FilterConfig filterConfig;
	AppUserDao appUserDao;
	
	@Inject
	public AdminAuthFilter(AppUserDao appUserDao){
		this.appUserDao = appUserDao;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		HttpSession session = request.getSession();

	    String thisURL = StringUtils.trimToEmpty(request.getRequestURI());
		
		if (thisURL.startsWith("/admin")){
			log.info("Check admin authentication ");
		
		    Object currentUser = session.getAttribute(Constants.SESSION_ADMIN_USER);
			if (currentUser == null){
				UserService userService = UserServiceFactory.getUserService();
				if (!userService.isUserLoggedIn()){
					RequestDispatcher rd = req.getRequestDispatcher("/login_admin");
					rd.forward(req, res);
					return;
				}
				else{
					if (!userService.isUserAdmin()){
						RequestDispatcher rd = req.getRequestDispatcher("/login_admin");
						rd.forward(req, res);
						return;
					}
					User user = userService.getCurrentUser();
					AppUser appUser = appUserDao.give(user.getUserId());
					if (!appUser.isSaved()) { //the user is not yet in database 
						appUser.setNickname(user.getNickname());
						appUser.setEmail(user.getEmail());
						appUserDao.put(appUser);
					}
					
					session.setAttribute(Constants.SESSION_ADMIN_USER, appUser);
					
					if ((thisURL.compareToIgnoreCase("/admin")==0) || (thisURL.compareToIgnoreCase("/admin/")==0)){
						RequestDispatcher rd = req.getRequestDispatcher("/admin/config");
						rd.forward(req, res);
						return;
					}

				}
			}
		}

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
}
