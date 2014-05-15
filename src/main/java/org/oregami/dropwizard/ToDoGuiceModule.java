package org.oregami.dropwizard;

import com.google.inject.AbstractModule;

public class ToDoGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		
//		bind(new TypeLiteral<GenericDAO<Game, Long>>() {}).to(GameDao.class);
//		bind(IUserService.class).to(UserServiceImpl.class);
//		bind(GameDAO.class);
		//bind(IUserService.class).to(UserServiceImpl.class);
	}

}
