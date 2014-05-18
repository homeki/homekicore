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

	public Channel() {

	}
	
	public Channel(int id, String name, DataType dataType) {
		this.id = id;
		this.name = name;
		this.dataType = dataType;
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
}
