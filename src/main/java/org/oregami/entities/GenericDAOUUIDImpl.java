package org.oregami.entities;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
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
        if (entity instanceof TopLevelEntity && CustomRevisionListener.context.get()!=null) {
            updateRevisionListener(entity);
        }
		return (P) entity.getId();
	}

	@Override
	public E findOne(P id) {
		return emf.get().find(getEntityClass(), id);
	}

	@Override
	@Transactional
	public void update(E entity) {
        if (entity instanceof TopLevelEntity && CustomRevisionListener.context.get()!=null) {
            updateRevisionListener(entity);
        }
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
        if (entity instanceof TopLevelEntity) {
            TopLevelEntity tle = (TopLevelEntity) entity;
            ServiceCallContext context = CustomRevisionListener.context.get();
            if (context != null) {
                context.setEntityDiscriminator(tle.getDiscriminator());
                context.setEntityId(entity.getId());
            }
        }
    }

}
