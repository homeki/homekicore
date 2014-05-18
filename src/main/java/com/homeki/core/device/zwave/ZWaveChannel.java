package com.homeki.core.device.zwave;

import com.homeki.core.device.Channel;
import org.zwave4j.ValueGenre;

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
	private int commandClassId;

	@Column
	private int instance;

	@Column
	private int index;

	public short getCommandClassId() {
		return (short)commandClassId;
	}

	public short getInstance() {
		return (short)instance;
	}

	public short getIndex() {
		return (short)index;
	}
}
