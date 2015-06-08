package com.homeki.core.device;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Channel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;

	@Column
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	@Column
	private String description;
	
	@Column(nullable = true)
	private String unit;
	
	@Column(nullable = true)
	private double scale;

	public Channel() {
		this.name = "";
		this.description = "";
		this.unit = "";
		this.scale = 1.0;
	}
	
	public Channel(int id, String name, DataType dataType) {
		this.id = id;
		this.name = name;
		this.dataType = dataType;
		this.unit = "";
		this.scale = 1.0;
	}
	
	public Channel(int id, String name, DataType dataType, String unit, double scale) {
		this.id = id;
		this.name = name;
		this.dataType = dataType;
		this.unit = unit;
		this.scale = scale;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public Device getDevice() {
		return device;
	}
	
	public String getUnit() {
		if(unit != null){
			return unit;
		}
		else {
			return "";
		}			
	}
	
	public double getScale() {
		if(scale != null) {
			return scale;
		}
		else {
			return 1.0;
		}
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public void setDevice(Device device) {
		this.device = device;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public void setScale(double scale) {
		this.scale = scale;
	}
}
