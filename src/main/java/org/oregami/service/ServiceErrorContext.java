package org.oregami.service;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Context af this error.
 * e.g. the field name of the web form
 */
@EqualsAndHashCode()
@ToString
public class ServiceErrorContext {

	/**
	 * Field name in web form e.g. "user.username"
	 */
	private String field;

    /**
     * The id of the object (if needed)
     */
    private String id;

	public ServiceErrorContext(String field) {
		this.field = field;
	}

    public ServiceErrorContext(String field, String id) {
        this.field = field;
        this.id = id;
    }
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}
	
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
