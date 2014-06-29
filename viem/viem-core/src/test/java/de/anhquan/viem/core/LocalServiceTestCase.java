package de.anhquan.viem.core;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import de.anhquan.config4j.ConfigFactory;
import de.anhquan.viem.core.dao.ConfigDao;
import de.anhquan.viem.core.model.AppConfig;

public abstract class LocalServiceTestCase extends AbstractModule{

	protected final LocalServiceTestHelper localServiceHelper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig()).setEnvIsAdmin(true)
			.setEnvIsLoggedIn(true);
	
	protected AppConfig config;
	protected Injector injector;
	protected ConfigDao configDao;
	
	public void basicSetUp(){
		localServiceHelper.setUp();
		injector = Guice.createInjector(this);		
		config = ConfigFactory.getConfig(AppConfig.class);
		
		configDao = injector.getInstance(ConfigDao.class);
	}
	
	public void basicTearDown(){
		localServiceHelper.tearDown();
	}
	
	@Override
	protected void configure() {
		guiceRegister();
	}

	abstract protected void guiceRegister();
	
	protected void register(Class cls){
		bind(cls).in(Singleton.class);
	}
}
