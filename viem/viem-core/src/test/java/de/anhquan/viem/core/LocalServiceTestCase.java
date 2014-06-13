package de.anhquan.viem.core;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import de.anhquan.viem.core.dao.AppSettingDao;

public abstract class LocalServiceTestCase extends AbstractModule{

	protected final LocalServiceTestHelper localServiceHelper = new LocalServiceTestHelper(
			new LocalUserServiceTestConfig()).setEnvIsAdmin(true)
			.setEnvIsLoggedIn(true);
	
	protected AppSettingDao appSettingDao;
	protected Injector injector;
	
	public void basicSetUp(){
		localServiceHelper.setUp();
		injector = Guice.createInjector(this);		
		appSettingDao = injector.getProvider(AppSettingDao.class).get();
		System.out.println("Setup");
	}
	
	public void basicTearDown(){
		localServiceHelper.tearDown();
	}
	
	@Override
	protected void configure() {
		register(AppSettingDao.class);
		guiceRegister();
	}

	abstract protected void guiceRegister();
	
	protected void register(Class cls){
		bind(cls).in(Singleton.class);
	}
}
