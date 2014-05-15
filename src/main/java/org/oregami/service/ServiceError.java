package org.oregami.service;


/**
 * Class that represents errors that occur during service layer calls. 
 * The messageName is a internal string used for translations.
 * The context defines in which field the error occurred (e.g. the web form field name).
 * 
 * @author twendelmuth
 * 
 */
public class ServiceError {

    private final ServiceErrorMessage messageName;
    
	private ServiceErrorContext context;

    public ServiceError(ServiceErrorContext context, ServiceErrorMessage messageName) {
    	this.context = context;
        this.messageName = messageName;
    }    
    
//    public ServiceError(ServiceErrorMessage message) {
//        this.messageName = message;
//    }

	public ServiceErrorContext getContext() {
		return context;
	}

	public void setContext(ServiceErrorContext context) {
		this.context = context;
	}
	
	@Override
	public String toString() {
		return "[" + context + ":" + messageName + "]";
	}
	
    public ServiceErrorMessage getMessageName() {
		return messageName;
	}
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ServiceError error = (ServiceError) obj;
        return (context == error.context
                     || (context != null && context.equals(error.context)))
                && 
                (messageName == error.messageName
                || (messageName != null && messageName.equals(error.messageName))
                );


    }
    
    
    @Override
    public int hashCode() {
    	return (context.getField() + ":" + messageName.name()).hashCode();
    }
    
    

}
