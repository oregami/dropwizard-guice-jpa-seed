package org.oregami.entities;

import javax.persistence.Entity;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

@Entity
@NamedQueries({@NamedQuery(name="Task.GetAll", query = "from Task t")})
public class Task extends BaseEntityUUID {

	private static final long serialVersionUID = -6910022407899412272L;

	private String name;
	
	private boolean finished = false;
	
	public Task(String name) {
		this.setName(name);
	}
	
	Task() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	

}
