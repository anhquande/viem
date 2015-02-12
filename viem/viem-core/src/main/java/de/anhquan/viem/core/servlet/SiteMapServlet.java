package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;

import de.anhquan.config4j.ConfigFactory;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.CategoryDao;
import de.anhquan.viem.core.dao.PageDao;
import de.anhquan.viem.core.dao.ProductDao;
import de.anhquan.viem.core.model.AppConfig;
import de.anhquan.viem.core.model.Category;
import de.anhquan.viem.core.model.Page;
import de.anhquan.viem.core.model.Product;

@ServletPath(value="/sitemap.xml")
public class SiteMapServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private AppConfig config;
	private PageDao pageDao;
	private ProductDao productDao;
	private CategoryDao categoryDao;

	@Inject
	public SiteMapServlet(PageDao pageDao, ProductDao productDao, CategoryDao categoryDao){
		this.pageDao = pageDao;
		this.productDao = productDao;
		this.categoryDao = categoryDao;
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
		String siteUrl = config.getSiteUrl();
		if (!siteUrl.endsWith("/"))
			siteUrl = siteUrl + "/";
		resp.setContentType("text/xml");
		PrintWriter writer;
		writer = resp.getWriter();
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		writer.write("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" xmlns:image=\"http://www.google.com/schemas/sitemap-image/1.1\">");
		
		List<Page> pages = pageDao.getAll();
		for (Page page : pages) {
			addUrl(writer, siteUrl+page.getName()+".html", "daily");
		}
		
		List<Product> products = productDao.getAll();
		for (Product product : products) {
			addUrl(writer, siteUrl+"product/"+product.getName(),"daily");
		}
		
		List<Category> categories = categoryDao.getAll();
		for (Category category : categories) {
			addUrl(writer, siteUrl+"category/"+category.getName(),"weekly");
		}
		
		writer.write("</urlset>");
		writer.flush();
		writer.close();
	}

	protected void addUrl(PrintWriter writer, String url, String frequency) {
		writer.write("<url><loc>"+url+"</loc><changefreq>"+frequency+"</changefreq></url>");
	}
}
