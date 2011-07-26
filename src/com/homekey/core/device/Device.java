package com.homekey.core.device;


public abstract class Device {
	private int id;
	private String name;

	public Device(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
}
