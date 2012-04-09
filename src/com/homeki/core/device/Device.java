package com.homeki.core.device;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.criterion.Restrictions;

import com.homeki.core.storage.Hibernate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Device {
	@Id
	@GeneratedValue
	private int id;
	
	@OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<HistoryPoint> historyPoints;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	@JoinTable(name = "device__trigger_abstract", joinColumns = { @JoinColumn(name = "device_id") }, inverseJoinColumns = { @JoinColumn(name = "trigger_abstract_id") })
	private Set<Trigger> triggers;
	
	@Column
	protected String internalId;
	
	@Column
	private String name;
	
	@Column
	private Date added;
	
	@Column
	private Boolean active;
	
	@Column
	private String description;
	
	public Device() {
		this.historyPoints = new HashSet<HistoryPoint>(0);
		this.name = "";
		this.internalId = "";
		this.added = new Date();
		this.description = "";
		this.active = true;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getInternalId() {
		return internalId;
	}
	
	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}
	
	public Date getAdded() {
		return added;
	}
	
	public void setAdded(Date added) {
		this.added = added;
	}
	
	public Set<HistoryPoint> getHistoryPoints() {
		return historyPoints;
	}
	
	public Set<Trigger> getTriggers() {
		return triggers;
	}
	
	public abstract String getType();
	
	public void preDelete() {
		
	}
	
	public static Device getByInternalId(Session session, String internalId) {
		return (Device) session.createCriteria(Device.class).add(Restrictions.eq("internalId", internalId)).uniqueResult();
	}
	
	public Boolean isActive() {
		return active;
	}
	
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public HistoryPoint getLatestHistoryPoint(int channel) {
		Session ses = Hibernate.currentSession();
		
		HistoryPoint p = (HistoryPoint)ses.createFilter(historyPoints, "where channel = ? order by registered desc")
				.setInteger(0, channel)
				.setMaxResults(1)
				.setFetchSize(1)
				.uniqueResult();
		
		return p;
	}
}
