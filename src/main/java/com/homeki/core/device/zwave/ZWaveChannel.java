package com.homeki.core.device.zwave;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.zwave4j.ValueGenre;
import org.zwave4j.ValueType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ZWaveChannel extends Channel {
	@Column
	@Enumerated(EnumType.STRING)
	private ValueGenre genre;

	@Column
	@Enumerated(EnumType.STRING)
	private ValueType valueType;

	@Column
	private int commandClassId;

	@Column
	private int instance;

	@Column
	private int zindex;

	public static ZWaveChannel getByDeviceAndIndex(Device device, short index) {
		Session ses = Hibernate.currentSession();
		return (ZWaveChannel)ses.createCriteria(ZWaveChannel.class)
			.add(Restrictions.eq("device", device))
			.add(Restrictions.eq("zindex", (int)index))
			.uniqueResult();
	}

	public short getCommandClassId() {
		return (short)commandClassId;
	}

	public short getInstance() {
		return (short)instance;
	}

	public short getIndex() {
		return (short)zindex;
	}

	public void setCommandClassId(short commandClassId) {
		this.commandClassId = commandClassId;
	}

	public void setInstance(short instance) {
		this.instance = instance;
	}

	public void setIndex(short index) {
		this.zindex = index;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public ValueGenre getGenre() {
		return genre;
	}

	public void setGenre(ValueGenre genre) {
		this.genre = genre;
	}
}
