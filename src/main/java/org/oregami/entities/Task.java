package org.oregami.entities;

import javax.persistence.*;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Task.GetAll", query = "from Task t")})
public class Task extends BaseEntityUUID {

	private static final long serialVersionUID = -6910022407899412272L;

	private String name;


	private String description;
	
	private boolean finished = false;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.EAGER)
    @JoinColumn
    private final Set<SubTask> subTasks = new HashSet<SubTask>();


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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void addSubTask(SubTask s) {
        getSubTasks().add(s);
    }
}
