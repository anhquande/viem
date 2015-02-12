package de.anhquan.viem.core.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.labs.repackaged.com.google.common.primitives.Ints;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;
import com.google.inject.Inject;

import de.anhquan.viem.core.ApplicationError;
import de.anhquan.viem.core.annotation.ServletPath;
import de.anhquan.viem.core.dao.ConfigDao;
import de.anhquan.viem.core.json.ConfigImporter;
import de.anhquan.viem.core.model.Action;
import de.anhquan.viem.core.model.AppConfig;
import de.anhquan.viem.core.model.ConfigEntity;

@ServletPath(value = "/admin/config")
public class ConfigAdminServlet extends BaseEntityAdminServlet<ConfigEntity> {

	private static final long serialVersionUID = 1L;

	public static final Logger log = Logger.getLogger(ConfigAdminServlet.class
			.getName());

	@Inject
	public ConfigAdminServlet(ConfigDao dao, ConfigImporter jsonImporter) {
		super(dao, jsonImporter);
	}

	@Override
	protected void doViewList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doViewList(request, response);
	}
	
	@Override
	protected void doClearAll(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String confirm = request.getParameter("confirm");
		if ((confirm != null)  && (confirm.compareToIgnoreCase("yes")==0)){
			log.info("Clear all configs and reset to default values");
			config.resetAll();
		}		
		renderJson(response, ApplicationError.OK);		
	}
	@Override
	protected void processExtraJsonRequest(Action action, ConfigEntity entity,
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		switch (action) {
		case GET_KEY:
			
			break;

		case SET_KEY:
			String valueType = StringUtils.trimToEmpty(request.getParameter("valueType"));
			String value = StringUtils.trimToEmpty(request.getParameter("value"));
			if (valueType.compareTo(String.class.getName())==0){
				entity.setStringValue(value);
			}
			else if (valueType.compareTo(Boolean.class.getName())==0){
				Boolean newVal = Boolean.parseBoolean(value);
				if (newVal!=null)
					entity.setBooleanValue(newVal);
			}
			else if (valueType.compareTo(Long.class.getName())==0){
				Long newVal = Longs.tryParse((StringUtils.trimToEmpty(value)));
				if (newVal!=null)
					entity.setLongValue(newVal);
			}
			else if (valueType.compareTo(Short.class.getName())==0){
				Short newVal = Longs.tryParse((StringUtils.trimToEmpty(value))).shortValue();
				if (newVal != null)
					entity.setShortValue(newVal);
			}
			else if (valueType.compareTo(Byte.class.getName())==0){
				Byte newVal = Longs.tryParse((StringUtils.trimToEmpty(value))).byteValue();
				if (newVal != null)
					entity.setByteValue(newVal);
			}
			else if (valueType.compareTo(Double.class.getName())==0){
				Double newVal = Doubles.tryParse((StringUtils.trimToEmpty(value)));
				if (newVal != null)
					entity.setDoubleValue(newVal );
			}
			else if (valueType.compareTo(Integer.class.getName())==0){
				Integer newVal = Ints.tryParse((StringUtils.trimToEmpty(value)));
				if (newVal != null)
					entity.setIntegerValue(newVal );
			}
			
			entityDao.put(entity);
			renderJson(response, ApplicationError.OK);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void doInitEntity(HttpServletRequest request,
			HttpServletResponse response) {
		AppConfig.class.getDeclaredMethods();
	}

}