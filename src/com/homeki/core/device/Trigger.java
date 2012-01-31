package com.homeki.core.device;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Trigger {
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column
	private String name;

	@Column
	private Integer newValue;
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getValue() {
		return newValue;
	}
	
	public void setValue(Integer value) {
		this.newValue = value;
	}
}
