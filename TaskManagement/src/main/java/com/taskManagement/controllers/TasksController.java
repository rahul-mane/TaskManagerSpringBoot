package com.taskManagement.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.xml.ws.http.HTTPBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.fabric.Response;
import com.taskManagement.entities.Task;
import com.taskManagement.entities.TaskStates;
import com.taskManagement.execeptions.TaskExceptions;
import com.taskManagement.repositories.TaskRepository;
import com.taskManagement.services.TaskService;
import com.taskManagement.util.CollectionUtil;
import com.taskManagement.validators.TaskValidator;

@Controller
@RestController
@RequestMapping(path = "/tasks") // This means URL's will start with /tasks after application path
public class TasksController {
	private TaskValidator validator = new TaskValidator();

	@Autowired
	private TaskService taskService;

	// POST : Create task
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> add(@RequestBody Task task) {
		if (validator.isValidTask(task) == false) {
			return new ResponseEntity<String>("In valid task", HttpStatus.BAD_REQUEST);
		}
		
		taskService.save(task);
		return new ResponseEntity<Task>(task, HttpStatus.CREATED);
	}

	// PUT : Update a task
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Task task) {
		if (validator.isValidTask(task) == false) {
			return new ResponseEntity<String>("In valid task", HttpStatus.BAD_REQUEST);
		}

		try {
			taskService.update(id, task);
			return new ResponseEntity<Task>(task, HttpStatus.ACCEPTED);
		} catch (TaskExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// PATH : Update state of task
	@RequestMapping(method = RequestMethod.PATCH, path = "/{id}")
	public ResponseEntity<?> updateState(@PathVariable("id") long id, @RequestBody Task task) {
		try {
			taskService.updateState(id, task);
			return new ResponseEntity<Task>(task, HttpStatus.ACCEPTED);
		} catch (TaskExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// GET : List of all task
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> all(@RequestParam(value = "state", defaultValue = "ALL") String state,
			org.springframework.data.domain.Pageable pageable) {
		try {
			TaskStates passedState = null;
			if(CollectionUtil.enumContains(TaskStates.class, state)) {
				passedState = TaskStates.valueOf(state);
			}
			List<Task> tasks = taskService.fetchAll(passedState, pageable);
			return new ResponseEntity<Iterable<Task>>(tasks, HttpStatus.OK);
		} catch (TaskExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// GET : Task details by id
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> get(@PathVariable("id") long id) {
		try {
			Task task = taskService.fetchById(id);
			return new ResponseEntity<Task>(task, HttpStatus.OK);
		} catch (TaskExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	// DELETE : Delete task
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
		try {
			taskService.delete(id);
			return new ResponseEntity<Task>(HttpStatus.ACCEPTED);
		} catch (TaskExceptions e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}
}
