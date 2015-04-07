package org.oregami.entities;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.joda.time.LocalDateTime;

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


    @Override
    @Transactional
    public void update(Task entity) {
        entity.setChangeTime(new LocalDateTime());
        super.update(entity);
    }

    @Override
    @Transactional
    public String save(Task entity) {
        entity.setChangeTime(new LocalDateTime());
        return super.save(entity);
    }

}
