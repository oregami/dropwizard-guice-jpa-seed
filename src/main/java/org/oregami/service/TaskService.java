package org.oregami.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.oregami.entities.Task;
import org.oregami.entities.TaskDao;
import org.oregami.validation.TaskValidator;

import com.google.inject.Inject;

public class TaskService {

	@Inject
	private TaskDao taskDao;
	
    public ServiceResult<Task> createNewTask(Task taskData) {
        TaskValidator validator = buildTaskValidator(taskData);

        List<ServiceError> errorMessages = validator.validateForCreation();

        Task task = null;

        if (errorMessages.size() == 0) {
            task = taskData;
            taskDao.save(task);
        }

        return new ServiceResult<Task>(taskData, errorMessages);
    }
    
    public ServiceResult<Task> updateTask(Task taskData) {
        TaskValidator validator = buildTaskValidator(taskData);

        List<ServiceError> errorMessages = validator.validateForUpdate();

        Task task = null;

        if (errorMessages.size() == 0) {
            task = taskData;
            taskDao.update(task);
        }

        return new ServiceResult<Task>(taskData, errorMessages);
    }

    public void deleteTask(String taskId) {
        Task task = taskDao.findOne(taskId);
        if (task==null) {
            throw new NoSuchElementException();
        }
        taskDao.delete(task);

    }

    private TaskValidator buildTaskValidator(Task newTask) {
		return new TaskValidator(this.taskDao, newTask);
	}
}
