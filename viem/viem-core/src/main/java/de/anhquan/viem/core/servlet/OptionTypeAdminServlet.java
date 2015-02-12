package de.anhquan.viem.core.servlet;

import java.util.logging.Logger;

import com.google.inject.Inject;

import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.OptionTypeDao;
import de.anhquan.viem.core.json.OptionTypeImporter;
import de.anhquan.viem.core.model.OptionType;

@ServletPath(value="/admin/optiontype")
public class OptionTypeAdminServlet extends BaseEntityAdminServlet<OptionType> {

	private static final long serialVersionUID = 1L;
	
	public static final Logger log = Logger.getLogger(OptionTypeAdminServlet.class.getName());
	
	@Inject
	public OptionTypeAdminServlet( 
			OptionTypeDao dao,
			OptionTypeImporter jsonImporter) {
		super(dao, jsonImporter);
	}

}