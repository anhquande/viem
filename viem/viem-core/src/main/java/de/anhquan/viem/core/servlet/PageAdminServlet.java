package de.anhquan.viem.core.servlet;

import java.util.logging.Logger;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.dao.PageDao;
import de.anhquan.viem.core.json.PageImporter;
import de.anhquan.viem.core.model.Page;

@ServletPath(value="/admin/page")
public class PageAdminServlet extends BaseEntityAdminServlet<Page>  {

	public static final Logger log = Logger.getLogger(PageAdminServlet.class
			.getName());

	@Inject
	public PageAdminServlet(AppSettingDao appSettingDao, 
			PageDao pageDao,
			PageImporter jsonImporter) {
		super(appSettingDao, pageDao, jsonImporter);
	}

	private static final long serialVersionUID = -6630427559567895991L;

}
