package com.homeki.core.device;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.device.abilities.Triggable;
import com.homeki.core.main.L;

@Entity(name="trigger")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Trigger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private Integer newValue;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	@JoinTable(name = "device__trigger", joinColumns = { @JoinColumn(name = "trigger_id") }, inverseJoinColumns = { @JoinColumn(name = "device_id") })
	private Set<Device> devices;
	
	public Trigger() {
		this.devices = new HashSet<Device>(0);
		this.name = "";
	}
	
	public Integer getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getNewValue() {
		return newValue;
	}
	
	public void setNewValue(Integer newValue) {
		this.newValue = newValue;
	}
	
	public Set<Device> getDevices() {
		return devices;
	}
	
	public void trigger() {
		for (Device d : devices) {
			if (d instanceof Triggable)
				((Triggable)d).trigger(newValue);
			else
				L.w("Tried to trigger non-triggable device with internal ID '" + d.getInternalId() + "'.");
		}
	}
	
	
	public abstract String getMeta();

	public abstract String getType();
}
