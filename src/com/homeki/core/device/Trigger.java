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
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.homeki.core.device.abilities.Dimmable;
import com.homeki.core.device.abilities.Switchable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Trigger {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_sequence")
	@SequenceGenerator(name = "trigger_sequence", sequenceName = "trigger_sequence")
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private Integer newValue;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	@JoinTable(name = "device_trigger", joinColumns = { @JoinColumn(name = "trigger_id") }, inverseJoinColumns = { @JoinColumn(name = "device_id") })
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
	
	public Integer getValue() {
		return newValue;
	}
	
	public void setValue(Integer value) {
		this.newValue = value;
	}
	
	public Set<Device> getDevices() {
		return devices;
	}
	
	// TODO: FULT MED INSTANCEOF
	public void trigger() {
		for (Device d : devices) {
			if (d instanceof Dimmable) {
				((Dimmable) d).dim(newValue, newValue !=0);
			} else if (d instanceof Switchable) {
				if (newValue != 0) {
					((Switchable) d).on();
				} else {
					((Switchable) d).off();
				}
			}
		}
	}
}
