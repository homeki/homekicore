package com.homeki.core.device;

import com.homeki.core.actions.ChangeChannelValueAction;
import com.homeki.core.conditions.ChannelValueCondition;
import com.homeki.core.events.ChannelValueChangedEvent;
import com.homeki.core.events.EventQueue;
import com.homeki.core.json.devices.JsonDevice;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.criterion.Restrictions;

import javax.persistence.*;
import java.util.*;

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
	private Set<HistoryPoint> historyPoints;
	
	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<ChannelValueCondition> channelValueConditions;
	
	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	private Set<ChangeChannelValueAction> changeChannelValueActions;

	@OneToMany(mappedBy = "device", orphanRemoval = true)
	@Cascade({ CascadeType.SAVE_UPDATE, CascadeType.DELETE })
	@LazyCollection(LazyCollectionOption.EXTRA)
	protected List<Channel> channels;
	
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

	@Column
	private int loggingInterval;
	
	public Device() {
		this.historyPoints = new HashSet<>();
		this.channelValueConditions = new HashSet<>();
		this.changeChannelValueActions = new HashSet<>();
		this.channels = new ArrayList<>();
		this.name = "";
		this.internalId = "";
		this.added = new Date();
		this.description = "";
		this.active = true;
		this.loggingInterval = 15 * 60 * 1000;
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

	public void setLoggingInterval(int loggingInterval) { this.loggingInterval = loggingInterval; }

	public int getLoggingInterval() { return this.loggingInterval; }

	public HistoryPoint getLatestHistoryPoint(int channel) {
		Session ses = Hibernate.currentSession();

		HistoryPoint p = (HistoryPoint)ses.createFilter(historyPoints, "where channel = ? order by registered desc")
				.setInteger(0, channel)
				.setMaxResults(1)
				.setFetchSize(1)
				.uniqueResult();
		
		return p;
	}
	
	public void addHistoryPoint(int channel, int value) {
		IntegerHistoryPoint dhp = new IntegerHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		dhp.setChannel(channel);
		historyPoints.add(dhp);
		
		if (id > 0)
			EventQueue.INSTANCE.add(new ChannelValueChangedEvent(id, channel, value));
		
	}
	
	public void addHistoryPoint(int channel, double value) {
		DoubleHistoryPoint dhp = new DoubleHistoryPoint();
		dhp.setDevice(this);
		dhp.setRegistered(new Date());
		dhp.setValue(value);
		dhp.setChannel(channel);
		historyPoints.add(dhp);
		
		if (id > 0)
			EventQueue.INSTANCE.add(new ChannelValueChangedEvent(id, channel, value));
	}
	
	public abstract List<Channel> getChannels();
	
	protected Channel getChannel(int channel) {
		List<Channel> channels = getChannels();
		
		for (Channel c : channels) {
			if (c.getId() == channel) return c;
		}
		
		throw new RuntimeException("Tried to operate on invalid channel " + channel + " on device with ID " + id + " (" + getType() + ").");
	}

	public JsonDevice toJson() {
		return new JsonDevice(this);
	}
}
