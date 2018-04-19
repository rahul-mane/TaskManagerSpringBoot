package com.taskManagement.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.taskManagement.entities.Task;
import com.taskManagement.entities.TaskStates;
import com.taskManagement.execeptions.TaskExceptions;
import com.taskManagement.repositories.TaskRepository;
import com.taskManagement.util.CollectionUtil;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public void save(Task task) {
		taskRepository.save(task);
	}

	public void update(long id, Task task) throws TaskExceptions {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (optionalTask.isPresent() == false) {
			throw new TaskExceptions("Task not found");
		}

		Task currentTask = optionalTask.get();
		currentTask.setState(task.getState());
		currentTask.setDescription(task.getDescription());

		taskRepository.save(currentTask);
	}

	public void updateState(long id, Task task) throws TaskExceptions {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (!optionalTask.isPresent()) {
			throw new TaskExceptions("Task not found");
		}

		Task currentTask = optionalTask.get();
		if (null != task.getState()) {
			currentTask.setState(task.getState());
		}
		taskRepository.save(currentTask);
	}

	public List<Task> fetchAll(TaskStates state, Pageable pageable) throws TaskExceptions {
		List<Task> tasks;
		if (null == state) {
			Page<Task> page = taskRepository.findAll(pageable);
			tasks = page.getContent();
		} else {
			tasks = (List<Task>) taskRepository.findByState(state);
		}

		return tasks;
	}

	public Task fetchById(long id) throws TaskExceptions {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (!optionalTask.isPresent()) {
			throw new TaskExceptions("Task not found");
		}
		return optionalTask.get();
	}

	public void delete(long id) throws TaskExceptions {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if (!optionalTask.isPresent()) {
			throw new TaskExceptions("Task not found");
		}

		taskRepository.deleteById(id);
	}
}
