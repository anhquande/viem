package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.anhquan.config4j.ConfigFactory;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.model.AppConfig;

@ServletPath(value="/robots.txt")
public class RobotsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AppConfig config;

	public RobotsServlet(){
		config = ConfigFactory.getConfig(AppConfig.class);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req,resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/plain");
		PrintWriter writer;
		writer = resp.getWriter();
		writer.write(config.getRobotsTxt());
		writer.flush();
		writer.close();
	}
}
