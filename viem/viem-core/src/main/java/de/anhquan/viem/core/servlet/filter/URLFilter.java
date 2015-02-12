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

import com.google.inject.Inject;
import com.restfb.util.StringUtils;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppUserDao;

@ServletPath("/*")
public class URLFilter implements Filter{

	private static final Logger log = Logger
			.getLogger(URLFilter.class.getName());

	AppUserDao appUserDao;
	
	@Inject
	public URLFilter(AppUserDao appUserDao){
		this.appUserDao = appUserDao;
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
	    String thisURL = StringUtils.trimToEmpty(request.getRequestURI());
		
		if (thisURL.isEmpty() || (thisURL.compareToIgnoreCase("/")==0)){
			RequestDispatcher rd = req.getRequestDispatcher("/index.html");
			rd.forward(req, res);
			return;
		}

		chain.doFilter(req, res);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
