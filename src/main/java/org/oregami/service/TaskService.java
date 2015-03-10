package org.oregami.service;

import com.google.inject.Inject;
import org.oregami.entities.CustomRevisionListener;
import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.validation.TaskValidator;

import java.util.List;
import java.util.NoSuchElementException;

public class TaskService {

	@Inject
	private TaskDao taskDao = null;

    public ServiceResult<Task> createNewTask(Task taskData, ServiceCallContext context) {
        TaskValidator validator = buildTaskValidator(taskData);

        List<ServiceError> errorMessages = validator.validateForCreation();

        Task task;

        if (errorMessages.size() == 0) {
            task = taskData;
            CustomRevisionListener.context.set(context);
            taskDao.save(task);
        }

        return new ServiceResult<>(taskData, errorMessages);
    }

    public ServiceResult<Task> updateTask(Task taskData, ServiceCallContext context) {
        TaskValidator validator = buildTaskValidator(taskData);

        List<ServiceError> errorMessages = validator.validateForUpdate();

        Task task;

        if (errorMessages.size() == 0) {
            task = taskData;
            CustomRevisionListener.context.set(context);
            taskDao.update(task);
        }

        return new ServiceResult<>(taskData, errorMessages);
    }

    public void deleteTask(String taskId) {
        Task task = taskDao.findOne(taskId);
        if (task==null) {
            throw new NoSuchElementException();
        }
        taskDao.delete(task);

    }

    private TaskValidator buildTaskValidator(Task newTask) {
		return new TaskValidator(newTask);
	}
}
