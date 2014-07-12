package org.oregami.entities;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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


    public List<Number> findRevisions(String id) {

        AuditReader reader = AuditReaderFactory.get(getEntityManager());

        List<Number> revisions = reader.getRevisions(Task.class, id);

//        for (Number n : revisions) {
//            Task tRev = reader.find(Task.class, id, n);
//            System.out.println(tRev);
//        }

        return revisions;

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


}
