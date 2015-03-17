package org.oregami.entities;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.oregami.data.RevisionInfo;
import org.oregami.service.ServiceCallContext;

public abstract class GenericDAOUUIDImpl<E extends BaseEntityUUID, P> implements
		GenericDAOUUID<E, P> {

	private final Provider<EntityManager> emf;

	@Inject
	public GenericDAOUUIDImpl(Provider<EntityManager> emf) {
		this.emf=emf;
	}

	Class<E> entityClass;

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public P save(E entity) {
		emf.get().persist(entity);
        updateRevisionListener(entity);
		return (P) entity.getId();
	}

	@Override
	public E findOne(P id) {
		return emf.get().find(getEntityClass(), id);
	}

	@Override
	@Transactional
	public void update(E entity) {
        updateRevisionListener(entity);
		emf.get().merge(entity);
	}

	@Override
	@Transactional
	public void delete(E entity) {
		emf.get().remove(entity);
	}

	@Override
	public EntityManager getEntityManager() {
		return emf.get();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		if (entityClass == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType paramType = (ParameterizedType) type;

				entityClass = (Class<E>) paramType.getActualTypeArguments()[0];

			} else {
				throw new IllegalArgumentException(
						"Could not guess entity class by reflection");
			}
		}
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		return this.emf.get().createNamedQuery(
				getEntityClass().getSimpleName() + ".GetAll").getResultList();
	}


    protected void updateRevisionListener(BaseEntityUUID entity) {
        if (entity.getClass().isAnnotationPresent(TopLevelEntity.class)) {
            ServiceCallContext context = CustomRevisionListener.context.get();
            if (context != null) {
                context.setEntityDiscriminator(entity.getClass().getAnnotation(TopLevelEntity.class).discriminator());
                context.setEntityId(entity.getId());
            }
        }
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

    public E findRevision(String id, Number revision) {

        AuditReader reader = AuditReaderFactory.get(getEntityManager());
        List<Number> revisions = reader.getRevisions(getEntityClass(), id);
        if (!revisions.contains(revision)) {
            return null;
        }
        E entity = reader.find(getEntityClass(), id, revision);
        return entity;

    }

}
