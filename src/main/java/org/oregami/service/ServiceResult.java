package org.oregami.service;

import java.util.ArrayList;
import java.util.List;

import lombok.ToString;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.oregami.entities.BaseEntityUUID;

/**
 * 
 * @param <T>
 */
@ToString
public class ServiceResult<T extends BaseEntityUUID> {

    private T result;

    private List<ServiceError> errors;

    public ServiceResult() {
        this(null);
    }

    public ServiceResult(T result) {
        this(result, new ArrayList<ServiceError>());
    }

    public ServiceResult(T result, List<ServiceError> errors) {
        this.result = result;
        this.errors = errors;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<ServiceError> getErrors() {
        return errors;
    }

    public void setErrors(List<ServiceError> errors) {
        this.errors = errors;
    }

    public boolean wasSuccessful() {
        return errors != null && errors.size() == 0;
    }

    public boolean hasErrors() {
        return !wasSuccessful();
    }

    public void addMessage(ServiceErrorContext context, ServiceErrorMessage message) {
        errors.add(new ServiceError(context, message));
    }
    
    public boolean containsError(ServiceError searchError) {
    	for (ServiceError error : errors) {
			if (error.equals(searchError)) {
				return true;
			}
		}
    	return false;
    }    

}
