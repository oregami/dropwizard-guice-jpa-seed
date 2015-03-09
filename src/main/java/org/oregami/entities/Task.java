package org.oregami.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.joda.time.LocalDateTime;
import org.oregami.data.CustomLocalDateTimeSerializer;

import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@NamedQueries({@NamedQuery(name="Task.GetAll", query = "from Task t")})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task extends BaseEntityUUID implements TopLevelEntity {

	private static final long serialVersionUID = -6910022407899412272L;

	private String name;

	private String description;

	private boolean finished = false;

    private LocalDateTime changeTime = null;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch= FetchType.EAGER)
    @JoinColumn
    private final Set<SubTask> subTasks = new HashSet<SubTask>();

    @OneToOne
    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    private Language language;


	public Task(String name) {
		this.setName(name);
	}

	public Task() {
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }


    public LocalDateTime getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(LocalDateTime changeTime) {
        this.changeTime = changeTime;
    }

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    public LocalDateTime getChangeTimeGui() {
        return changeTime;
    }

    @Override
    public Discriminator getDiscriminator() {
        return Discriminator.TASK;
    }

}
