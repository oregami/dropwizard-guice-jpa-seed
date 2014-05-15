package org.oregami.service;

/**
 * Context af this error.
 * e.g. the field name of the web form
 */
public class ServiceErrorContext {

	/**
	 * Field name in web form e.g. "user.username"
	 */
	private String field;

	public ServiceErrorContext(String field) {
		this.field = field;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		return field;
	}
	
	@Override
	public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        ServiceErrorContext context = (ServiceErrorContext) obj;
        return (field == context.field
                || (field != null && field.equals(context.field))
                );
	}
	
	@Override
	public int hashCode() {
		if (field==null) {
			return 0;
		}
		return field.hashCode();
	}
}
