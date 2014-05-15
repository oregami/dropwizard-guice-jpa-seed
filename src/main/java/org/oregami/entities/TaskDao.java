package org.oregami.entities;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class TaskDao extends GenericDAOUUIDImpl<Task, String>{

	@Inject
	public TaskDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass=Task.class;
	}
	
	public Task findByExactName(String name) {
		Task t = (Task) getEntityManager()
        		.createNativeQuery("SELECT * FROM Task t where lower(r.name) = :value ", Task.class).setParameter("value", name.toLowerCase()).getSingleResult(); 
        return t;
    }


}
