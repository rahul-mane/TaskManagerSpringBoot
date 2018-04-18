package com.taskManagement.repositories;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.taskManagement.entities.Task;
import com.taskManagement.entities.TaskStates;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long>  {
	public Iterable<Task> findByState(@Param("state") TaskStates state);
	
}
