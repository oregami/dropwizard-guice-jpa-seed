package org.oregami.entities;

import com.google.inject.Inject;
import com.google.inject.Provider;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RevisionEntityDao {

    private final Provider<EntityManager> emf;

    @Inject
	public RevisionEntityDao(Provider<EntityManager> emf) {
		this.emf = emf;
	}


    public List<CustomRevisionEntity> findAll() {
        int limit = 100;
        Query query = emf.get().createQuery(
                "SELECT e FROM CustomRevisionEntity e order by timestamp DESC")
                .setMaxResults(limit);
        return (List<CustomRevisionEntity>) query.getResultList();
    }

}
