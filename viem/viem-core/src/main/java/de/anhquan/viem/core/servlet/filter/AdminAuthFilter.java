package de.anhquan.viem.core.servlet.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppUserDao;

@ServletPath("/admin/*")
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

	    String thisURL = request.getRequestURI();
		log.info("Destination URL is: " + thisURL );

		if (thisURL.startsWith("/admin")){
			log.info("Check admin authentication ");
		/*
		    Object currentUser = session.getAttribute(Constants.SESSION_ADMIN_USER);
			if (currentUser == null){
				UserService userService = UserServiceFactory.getUserService();
				if (!userService.isUserLoggedIn()){
					RequestDispatcher rd = req.getRequestDispatcher("/login2admin");
					rd.forward(req, res);
					return;
				}
				else{
					User user = userService.getCurrentUser();
//					String email = user.getEmail();
//					AppUser appUser = appUserDao.give(email);
//					if (!appUser.isSaved()) { //the user is not yet in database 
//						appUser.setNickName(user.getNickname());
//						appUser.setUserId(user.getUserId());
//						appUserDao.put(appUser);
//					}
						
					session.setAttribute(Constants.SESSION_ADMIN_USER, user);
				}
			}
			*/
		}

		chain.doFilter(req, res);

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
