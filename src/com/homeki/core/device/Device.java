package com.homeki.core.device;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.criterion.Restrictions;

import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.storage.Hibernate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Device {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected int id;
	
	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<HistoryPoint> historyPoints;
	
	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<ChannelValueCondition> channelValueConditions;
	
	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected Set<ChangeChannelValueAction> changeChannelValueActions;
	
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
		this.historyPoints = new HashSet<HistoryPoint>();
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
	
	public abstract String getType();
	
	public void preDelete() {
		
	}
	
	public static Device getByInternalId(String internalId) {
		Session ses = Hibernate.currentSession();
		return (Device)ses.createCriteria(Device.class).add(Restrictions.eq("internalId", internalId)).uniqueResult();
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
	
	public String[] getAbilities() {
		return new String[0];
	}
	
	public abstract List<Channel> getChannels();
}
