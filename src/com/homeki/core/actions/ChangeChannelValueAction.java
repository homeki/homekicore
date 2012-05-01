package com.homeki.core.actions;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.abilities.Settable;

@Entity
public  class ChangeChannelValueAction extends Action{
	@Column
	private int channel;

	@Column
	private int deviceId;
	
	@Column
	private int value;
	
	@Override
	public void execute(Session ses) {
		Settable s = (Settable) ses.get(Device.class, deviceId);
		s.set(channel, value);
	}
}
