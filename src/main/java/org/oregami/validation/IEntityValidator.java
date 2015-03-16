package org.oregami.validation;

import org.oregami.service.ServiceError;

import java.util.Collection;
import java.util.List;

/**
 * Created by sebastian on 16.03.15.
 */
public interface IEntityValidator {

    public Collection<ServiceError> validateRequiredFields();

    public List<ServiceError> validateForUpdate();

    public List<ServiceError> validateForCreation();
}
