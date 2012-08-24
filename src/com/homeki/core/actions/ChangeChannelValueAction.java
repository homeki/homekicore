package com.homeki.core.actions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;

@Entity
public class ChangeChannelValueAction extends Action {
	@Column
	private int channel;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "device_id")
	private Device device;
	
	@Column
	private Number value;
	
	public ChangeChannelValueAction() {
		
	}
	
	public ChangeChannelValueAction(Device device, int channel, Number value) {
		this.device = device;
		this.channel = channel;
		this.value = value;
	}
	
	@Override
	public void execute(Session ses) {
//		Settable s = (Settable) ses.get(Device.class, deviceId);
//		s.set(channel, value);
		L.i("Triggered change channel value action on device '" + device.getName() + "', channel " + channel + " to value " + value + ".");
	}

	@Override
	public String getType() {
		return "changechannelvalue";
	}
}
