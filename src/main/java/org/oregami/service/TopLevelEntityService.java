package org.oregami.service;

import org.oregami.entities.BaseEntityUUID;
import org.oregami.entities.CustomRevisionListener;
import org.oregami.entities.GenericDAOUUID;
import org.oregami.validation.IEntityValidator;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class TopLevelEntityService<T extends BaseEntityUUID> {

    public abstract GenericDAOUUID<T, String> getDao();

    public ServiceResult<T> createNewEntity(T entityData, ServiceCallContext context) {

        IEntityValidator validator = buildEntityValidator(entityData);
        List<ServiceError> errorMessages = validator.validateForCreation();
        if (errorMessages.size() == 0) {
            T entity = entityData;
            CustomRevisionListener.context.set(context);
            getDao().save(entity);
        }

        return new ServiceResult<>(entityData, errorMessages);
    }

    public ServiceResult<T> updateEntity(T entityData, ServiceCallContext context) {

        IEntityValidator validator = buildEntityValidator(entityData);
        List<ServiceError> errorMessages = validator.validateForUpdate();
        if (errorMessages.size() == 0) {
            T entity = entityData;
            CustomRevisionListener.context.set(context);
            getDao().update(entity);
        }
        return new ServiceResult<>(entityData, errorMessages);
    }

    public void deleteEntity(String id) {
        T entity = getDao().findOne(id);
        if (entity==null) {
            throw new NoSuchElementException();
        }
        getDao().delete(entity);
    }

    public abstract IEntityValidator buildEntityValidator(T entity);

}
