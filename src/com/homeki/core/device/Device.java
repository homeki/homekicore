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
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.criterion.Restrictions;

import com.homeki.core.storage.HistoryPoint;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type", discriminatorType=DiscriminatorType.STRING)
public abstract class Device {
	@Id
	@GeneratedValue
	private int id;
	
	@OneToMany(mappedBy="device",cascade=CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<HistoryPoint> historyPoints;
	
	@Column
	protected String internalId;
	
	@Column
	private String name;
	
	@Column
	private Date added;
	
	public Device() {
		this.historyPoints = new HashSet<HistoryPoint>(0);
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
		return this.historyPoints;
	}
	
	public abstract String getType();
	
	public static Device getByInternalId(Session session, String internalId) {
		return (Device)session.createCriteria(Device.class).add(Restrictions.eq("internalId", internalId)).uniqueResult();
	}
}
