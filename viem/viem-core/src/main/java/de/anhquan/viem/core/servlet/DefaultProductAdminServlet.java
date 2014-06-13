package de.anhquan.viem.core.servlet;

import java.util.logging.Logger;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.AppSettingDao;
import de.anhquan.viem.core.dao.DefaultProductDao;
import de.anhquan.viem.core.json.DefaultProductImporter;
import de.anhquan.viem.core.model.DefaultProduct;

/**
 * This servlet manages products in the backend
 * @author anhquan
 *
 */
@ServletPath(value = "/admin/defaultproduct")
public class DefaultProductAdminServlet extends BaseEntityAdminServlet<DefaultProduct> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(DefaultProductAdminServlet.class.getName());

	@Inject
	public DefaultProductAdminServlet(final AppSettingDao appSettingDao, 
								final DefaultProductDao defaultProductDao, 
								final DefaultProductImporter jsonImporter) {
		super(appSettingDao, defaultProductDao,jsonImporter);
	}


}
