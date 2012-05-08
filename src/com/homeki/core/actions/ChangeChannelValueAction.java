package com.homeki.core.actions;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.Session;

@Entity
public  class ChangeChannelValueAction extends Action{
	@SuppressWarnings("unused")
	@Column
	private int channel;

	@SuppressWarnings("unused")
	@Column
	private int deviceId;
	
	@SuppressWarnings("unused")
	@Column
	private int value;
	
	@Override
	public void execute(Session ses) {
//		Settable s = (Settable) ses.get(Device.class, deviceId);
//		s.set(channel, value);
		System.out.println("BOOOOOOOOOOOOOM");
	}
}
