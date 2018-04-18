package com.taskManagement.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Optional;import javax.xml.ws.http.HTTPBinding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.taskManagement.repositories.TaskRepository;
import com.taskManagement.validators.TaskValidator;

@RestController
@RequestMapping(path="/tasks") // This means URL's will start with /tasks after application path
public class TasksController {

	@Autowired
	private TaskRepository taskRepository;
	private TaskValidator validator = new TaskValidator();
	
	//POST :  Create task
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<?> addNewTask(@RequestBody Task task) {
		if(validator.isValidTask(task) == false) {
			return new ResponseEntity<String>("In valid task",HttpStatus.BAD_REQUEST);
		}
		taskRepository.save(task);
		return new ResponseEntity<Task>(task,HttpStatus.CREATED);
	}
	
	//PUT : Update a task
	@RequestMapping(method=RequestMethod.PUT,path = "/{id}")
	public ResponseEntity<?> updateTask(@PathVariable("id") long id, @RequestBody Task task){
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent() == false) {
			return new ResponseEntity<String>("Not found",HttpStatus.NOT_FOUND);
		}
		
		if(validator.isValidTask(task) == false) {
			return new ResponseEntity<String>("In valid task",HttpStatus.BAD_REQUEST);
		}
		
		Task currentTask = optionalTask.get();
		currentTask.setState(task.getState());
		currentTask.setDescription(task.getDescription());
		
		taskRepository.save(currentTask);
		return new ResponseEntity<Task>(currentTask,HttpStatus.ACCEPTED);
	}
	
	//PATH : Update state of task
	@RequestMapping(method=RequestMethod.PATCH,path = "/{id}")
	public ResponseEntity<?> updateStateOfTask(@PathVariable("id") long id,@RequestBody Task task){
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent() == false) {
			return new ResponseEntity<String>("Not found",HttpStatus.NOT_FOUND);
		}
		
		Task currentTask = optionalTask.get();
		if(null != task.getState()) {
			currentTask.setState(task.getState());
		}
		taskRepository.save(currentTask);
		return new ResponseEntity<Task>(currentTask,HttpStatus.ACCEPTED);
	}
	
	//GET : List of all task
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<?> getAllTasks(@RequestParam(value="state",defaultValue="ALL") String state){
		List<Task>  tasks;
		if(state.equalsIgnoreCase("ALL")) {
			tasks = (List<Task>) taskRepository.findAll();
		}
		else {
			TaskStates passedState = TaskStates.valueOf(state);
			tasks = (List<Task>) taskRepository.findByState(passedState);
		}
		
		
		if(this.sizeOfIterable(tasks) == 0) {
			return new ResponseEntity<String>("No tasks found",HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Iterable<Task>>(tasks,HttpStatus.OK);
	}
	
	//GET : Task details by id
	@RequestMapping(method=RequestMethod.GET,path="/{id}")
	public ResponseEntity<?> getTask(@PathVariable("id") long id) {
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent() == false) {
			return new ResponseEntity<String>("Not found",HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Task>(optionalTask.get(),HttpStatus.ACCEPTED);
	}
	

	//DELETE : Delete task
	@RequestMapping(method=RequestMethod.DELETE,path = "/{id}")
	public ResponseEntity<?> deleteTask(@PathVariable("id") long id){
		Optional<Task> optionalTask = taskRepository.findById(id);
		if(optionalTask.isPresent() == false) {
			return new ResponseEntity<String>("Not found",HttpStatus.NOT_FOUND);
		}
		
		taskRepository.deleteById(id);
		return new ResponseEntity<Task>(HttpStatus.ACCEPTED);
	}
	
	
	//Helpers.
	 private int sizeOfIterable(Iterable<?> it) {
		  if (it instanceof Collection)
		    return ((Collection<?>)it).size();

		  // else iterate

		  int i = 0;
		  for (Object obj : it) i++;
		  return i;
	}
}
