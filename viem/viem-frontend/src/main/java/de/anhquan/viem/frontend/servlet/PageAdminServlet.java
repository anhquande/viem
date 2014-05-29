package de.anhquan.viem.frontend.servlet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.inject.Inject;

import de.anhquan.viem.core.dao.cache.AppSettingManager;
import de.anhquan.viem.core.servlet.ServletPath;
import de.anhquan.viem.frontend.dao.PageDao;
import de.anhquan.viem.frontend.dao.cache.PageManager;
import de.anhquan.viem.frontend.model.Page;

@ServletPath(basePath="/admin/page")
public class PageAdminServlet extends BaseEntityAdminServlet<Page>  {

	public static final Logger log = Logger.getLogger(PageAdminServlet.class
			.getName());

	@Inject
	public PageAdminServlet(AppSettingManager settingManager,
			PageManager manager, PageDao dao) {
		super(settingManager,manager, dao );
	}

	private static final long serialVersionUID = -6630427559567895991L;

	@Override
	protected void doUpdateEntity(Page page, HttpServletRequest request) {
		page.setTitle(StringUtils.trimToEmpty(request
				.getParameter("title")));
		page.setDescription(StringUtils.trimToEmpty(request
				.getParameter("description")));
		page.setTemplate(StringUtils.trimToEmpty(request
				.getParameter("template")));
		page.setContent(StringUtils.trimToEmpty(request
				.getParameter("content")));
	}

	@Override
	protected Page doCreateEntity(HttpServletRequest request) {
		Page page = new Page();
		SimpleDateFormat df = new SimpleDateFormat("dd.MMM HH:mm:sss");
		String now = df.format(new Date());
		page.setTitle("Newpage-" + now);
		String name = StringUtils.trimToEmpty(request.getParameter("name"));
		if (name.isEmpty())
			name = "newpage-" + now;
		page.setName(name);

		String title = StringUtils.trimToEmpty(request
				.getParameter("title"));
		if (title.isEmpty())
			title = "New Page - " + now;
		page.setTitle(title);

		String template = StringUtils.trimToEmpty(request
				.getParameter("template"));
		if (template.isEmpty())
			template = "default";
		page.setTemplate(template);

		page.setDescription(StringUtils.trimToEmpty(request
				.getParameter("description")));
		page.setContent(StringUtils.trimToEmpty(request
				.getParameter("content")));

		return page;
	}

}
