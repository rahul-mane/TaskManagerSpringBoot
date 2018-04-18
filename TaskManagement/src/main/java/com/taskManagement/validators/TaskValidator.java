package com.taskManagement.validators;

import com.taskManagement.entities.Task;
import com.taskManagement.entities.TaskStates;

public class TaskValidator {
	public boolean isValidTask(Task task) {
		if(task.getState() == TaskStates.TODO || task.getState() == TaskStates.INPROGRESS || task.getState() == TaskStates.COMPLETED) {
			return true;
		}
		else {
			return false;
		}
	}
}
