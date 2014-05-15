package org.oregami.dropwizard;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.oregami.data.DatabaseFiller;
import org.oregami.resources.TaskResource;

import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.hubspot.dropwizard.guice.GuiceBundle;

public class ToDoApplication extends Application<ToDoConfiguration> {

	public static final String JPA_UNIT = "data";
	// "dataMysql";

	private static final JpaPersistModule jpaPersistModule = new JpaPersistModule(JPA_UNIT);

	private GuiceBundle<ToDoConfiguration> guiceBundle;
	
	PersistService persistService = null;
	
	public static void main(String[] args) throws Exception {
		new ToDoApplication().run(args);
	}

	@Override
	public String getName() {
		return "hello-world";
	}

	@Override
	public void initialize(Bootstrap<ToDoConfiguration> bootstrap) {
		guiceBundle = GuiceBundle.<ToDoConfiguration> newBuilder()
				.addModule(new ToDoGuiceModule())
				.addModule(jpaPersistModule).enableAutoConfig("org.oregami")
				.setConfigClass(ToDoConfiguration.class).build();

		bootstrap.addBundle(guiceBundle);

		persistService = guiceBundle.getInjector().getInstance(PersistService.class);
		DatabaseFiller.getInstance().initData();
		
	}

	@Override
	public void run(ToDoConfiguration configuration, Environment environment) {
		
		Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
	    filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
	    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
	    filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
	    filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin");
	    filter.setInitParameter("allowCredentials", "true");
//	    
//	    filter.setInitParameter("allow", "GET,PUT,POST,DELETE,OPTIONS");
//	    filter.setInitParameter("preflightMaxAge", "5184000"); // 2 months
		
	    environment.servlets().addFilter("persistFilter", guiceBundle.getInjector().getInstance(PersistFilter.class)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
	    
		environment.jersey().register(guiceBundle.getInjector().getInstance(TaskResource.class));
		
	}
	
	public static JpaPersistModule createJpaModule() {
		return jpaPersistModule;
	}	

}