package com.homeki.core.device;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;


@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class HistoryPoint {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Device device;
	
	@Column
	private Date registered;
	
	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	public int getId() {
		return this.id;
	}
	
	public abstract Object getValue();
}
