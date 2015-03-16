package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.GenericDAOUUID;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.validation.IEntityValidator;
import org.oregami.validation.TaskValidator;

public class TaskService extends TopLevelEntityService<Task> {

    @Inject
    TaskDao dao;

    @Override
    public GenericDAOUUID<Task, String> getDao() {
        return dao;
    }

    @Override
    public IEntityValidator buildEntityValidator(Task entity) {
        return new TaskValidator(entity);
    }
}
