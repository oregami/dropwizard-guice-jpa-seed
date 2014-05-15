package org.oregami.data;

import org.apache.log4j.Logger;
import org.oregami.dropwizard.ToDoApplication;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.jpa.JpaPersistModule;

/**
 * Class to fill the database with some sample entities.
 */
public class DatabaseFiller {

	Logger logger = Logger.getLogger(DatabaseFiller.class);
	
	static DatabaseFiller instance = null;
	
	static Injector injector;
	
	
	@Transactional
	public void initData() {
		addRegions();
	}
	
	public static DatabaseFiller getInstance() {
		
		if (instance==null) {
			JpaPersistModule jpaPersistModule = ToDoApplication.createJpaModule();
			injector = Guice.createInjector(jpaPersistModule);
			instance = injector.getInstance(DatabaseFiller.class);
			PersistService persistService = injector.getInstance(PersistService.class);
			persistService.start();
		}
		return instance;
	}	

	private void addRegions() {
		
	}
}
