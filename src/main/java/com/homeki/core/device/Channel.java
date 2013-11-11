package com.homeki.core.device;

public class Channel {
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String BOOL = "bool";
	public static final String BYTE = "byte";
	public static final String DOUBLE = "double";
	public static final String STRING = "string";
	
	public int id;
	public String name;
	public String dataType;
	
	public Channel(int id, String name, String dataType) {
		this.id = id;
		this.name = name;
		this.dataType = dataType;
	}
}
