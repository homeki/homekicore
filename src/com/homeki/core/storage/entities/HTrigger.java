package com.homeki.core.storage.entities;

public class HTrigger {
	private Integer id;
	private String name;
	private Integer value;
	private HCondition condition;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public HCondition getCondition() {
		return condition;
	}
	
	public void setCondition(HCondition condition) {
		this.condition = condition;
	}
}
