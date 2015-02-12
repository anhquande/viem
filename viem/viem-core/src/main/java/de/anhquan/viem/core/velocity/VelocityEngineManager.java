package de.anhquan.viem.core.velocity;

import java.util.logging.Logger;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.ToolManager;

public final class VelocityEngineManager {
	public static final Logger log = Logger.getLogger(VelocityEngineManager.class.getName());
	static VelocityEngine engine = null;
	static Context context = null;
	
	private static void init(){
		
			log.info("Init VelocityEngine ...");
			engine = new VelocityEngine();
			engine.setProperty("resource.loader", "file");
			engine.setProperty("file.resource.loader.class", "de.anhquan.viem.core.velocity.VelocityResourceLoader");
			engine.setProperty(RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,"org.apache.velocity.runtime.log.Log4JLogChute" );
			engine.setProperty("class.resource.loader.cache", false);
			engine.setProperty("url.resource.loader.cache", false);
			engine.setProperty("file.resource.loader.cache", false);
			engine.setProperty("file.resource.loader.cache", false);
			engine.setProperty("velocimacro.library.autoreload", true);
			
			engine.setProperty("runtime.log.logsystem.log4j.logger", log.getName());
			try{
				engine.init();
				ToolManager manager = new ToolManager();
				manager.configure("WEB-INF/tools.xml"); 
				context = manager.createContext(); 
			}
			catch (Exception e){
				System.out.println(e.getMessage());
			}
			
	}
	
	public static Template getTemplate(String template){
		if (engine==null)
			init();
		return engine.getTemplate( template, "UTF-8" );
	}
	
	public static Context getContext(){
		if (context == null)
			init();
		return context;
	}
}