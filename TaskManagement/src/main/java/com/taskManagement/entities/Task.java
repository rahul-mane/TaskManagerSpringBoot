package com.taskManagement.entities;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Null;

@Entity
public class Task {

	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id
	private long id;
	
	@Enumerated(EnumType.STRING)
	private TaskStates state;
	
	private String description;
	
	
	public void setId(long id) {
		this.id = id;
	}
	
	public long getId() {
		return this.id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public void setState(TaskStates state) {
		this.state  = state;
	}
	public TaskStates getState() {
		return this.state;
	}
}
