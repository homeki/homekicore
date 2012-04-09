package com.homeki.core.device;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
public abstract class HistoryPoint {
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;
	
	@Column
	private Date registered;
	
	@Column
	private int channel;
	
	public HistoryPoint(int channel) {
		this.channel = channel;
	}
	
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
	
	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel) {
		this.channel = channel;
	}
	
	public int getId() {
		return this.id;
	}
	
	public abstract Object getValue();
}
