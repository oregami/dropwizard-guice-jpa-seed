package org.oregami.entities;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.joda.time.LocalDateTime;
import org.oregami.data.RevisionInfo;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class LanguageDao extends GenericDAOUUIDImpl<Language, String>{

	@Inject
	public LanguageDao(Provider<EntityManager> emf) {
		super(emf);
		entityClass=Language.class;
	}

    public Language findByExactName(String name) {
        Language l = (Language) getEntityManager()
                .createNativeQuery("SELECT * FROM Language t where lower(t.name) = :value ", Language.class).setParameter("value", name.toLowerCase()).getSingleResult();
        return l;
    }


    public List<RevisionInfo> findRevisions(String id) {
        List<RevisionInfo> list = new ArrayList<>();
        AuditReader reader = AuditReaderFactory.get(getEntityManager());
        List<Number> revisions = reader.getRevisions(Language.class, id);
        for (Number n : revisions) {
            CustomRevisionEntity revEntity = reader.findRevision(CustomRevisionEntity.class, n);
            list.add(new RevisionInfo(n, revEntity));
        }
        return list;
    }

    public Language findRevision(String id, Number revision) {

        AuditReader reader = AuditReaderFactory.get(getEntityManager());

        List<Number> revisions = reader.getRevisions(entityClass, id);
        if (!revisions.contains(revision)) {
            return null;
        }
        Language tRev = reader.find(entityClass, id, revision);
        return tRev;

    }

    @Override
    @Transactional
    public void update(Language entity) {
        updateRevisionListener(entity);
        entity.setChangeTime(new LocalDateTime());
        super.update(entity);
    }

    @Override
    @Transactional
    public String save(Language entity) {
        updateRevisionListener(entity);
        entity.setChangeTime(new LocalDateTime());
        return super.save(entity);
    }


}
