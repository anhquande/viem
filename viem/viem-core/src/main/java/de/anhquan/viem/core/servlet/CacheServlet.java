package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.dao.cache.CacheManager;

public class CacheServlet extends HttpServlet {

	private static final long serialVersionUID = 3080454677031239168L;
	
	private List<CacheManager> managers;
	
	@Inject
	public CacheServlet(AppSettingManager settingManager){
		managers = new ArrayList<CacheManager>();
		managers.add(settingManager);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		handleRequest(req, resp);
	}
	
	protected void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String action = req.getParameter("action");
		if ((action == null) || action.isEmpty()){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "require 'action' param");
			return;
		}
		
		String entity = req.getParameter("entity");
		if ((entity == null) || entity.isEmpty()){
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Please be careful! Which cache entity do you want to work with?");
			return;
		}

		if (action.compareToIgnoreCase("write")==0){
			if (entity.compareToIgnoreCase("all")==0){
				for (CacheManager cacheManager : managers) {
					cacheManager.writeToDataStore();
				}
			}
			else
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sorry! The cache entity is invalid.");
		}
		else if (action.compareToIgnoreCase("read")==0){
			if (entity.compareToIgnoreCase("all")==0){
				for (CacheManager cacheManager : managers) {
					cacheManager.readFromDataStore();
				}
			}
			else
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sorry! The cache entity is invalid.");
		}
		else if (action.compareToIgnoreCase("clear")==0){
			if (entity.compareToIgnoreCase("all")==0){
				for (CacheManager cacheManager : managers) {
					cacheManager.clearCache();
				}
			}
			else
				resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Sorry! The cache entity is invalid.");
		}
		else{
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "do not understand the action ");
			return;
		}

		resp.sendError(HttpServletResponse.SC_OK, "Everything is fine");
	}
}
