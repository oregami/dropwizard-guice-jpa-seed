package org.oregami.entities;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.joda.time.LocalDateTime;
import org.oregami.data.RevisionInfo;

import java.util.*;

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


    public List<RevisionInfo> findRevisions(String id) {

        List<RevisionInfo> list = new ArrayList<>();

        AuditReader reader = AuditReaderFactory.get(getEntityManager());

        List<Number> revisions = reader.getRevisions(Task.class, id);

        for (Number n : revisions) {
            CustomRevisionEntity revEntity = reader.findRevision(CustomRevisionEntity.class, n);
            list.add(new RevisionInfo(n, revEntity));
        }

        return list;

    }

    public Task findRevision(String id, Number revision) {

        AuditReader reader = AuditReaderFactory.get(getEntityManager());

        List<Number> revisions = reader.getRevisions(Task.class, id);
        if (!revisions.contains(revision)) {
            return null;
        }
        Task tRev = reader.find(Task.class, id, revision);
        return tRev;

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
