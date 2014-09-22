package org.oregami.service;


import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Class that represents errors that occur during service layer calls. 
 * The messageName is a internal string used for translations.
 * The context defines in which field the error occurred (e.g. the web form field name).
 * 
 */
@EqualsAndHashCode()
@ToString
public class ServiceError {

    private final ServiceErrorMessage messageName;
    
	private ServiceErrorContext context;

    public ServiceError(ServiceErrorContext context, ServiceErrorMessage messageName) {
    	this.context = context;
        this.messageName = messageName;
    }    
    
	public ServiceErrorContext getContext() {
		return context;
	}

	public void setContext(ServiceErrorContext context) {
		this.context = context;
	}
	
    public ServiceErrorMessage getMessageName() {
		return messageName;
	}
    
    

}
