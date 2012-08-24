package com.homeki.core.actions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

import com.homeki.core.device.Device;

@Entity
public class ChangeChannelValueAction extends Action {
	@Column
	private int channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;
	
	@Column
	private int value;
	
	@Override
	public void execute(Session ses) {
//		Settable s = (Settable) ses.get(Device.class, deviceId);
//		s.set(channel, value);
		System.out.println("BOOOOOOOOOOOOOM");
	}
}
