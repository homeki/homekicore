package com.homeki.core.actions;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.Session;

import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;

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
		try {
			device = Hibernate.unproxy(device);
			Settable s = (Settable)device;
			s.set(channel, value.intValue());
			L.i("Triggered change channel value action on device '" + device.getName() + "', channel " + channel + " to value " + value + ".");
		}
		catch (ClassCastException ex) {
			L.e("Device with ID " + device.getId() + " and name '" + device.getName() + "' was about to be switched in a ChangeChannelValueAction, but the device is not Settable.");
		}
		catch (Exception ex) {
			L.e("Unknown exception occured when executing ChangeChannelValueAction on device with ID " + device.getId() + " and name '" + device.getName() + "'.", ex);
		}
	}
	
	public int getDeviceId() {
		return device.getId();
	}
	
	public Number getValue() {
		return value;
	}
	
	public int getChannel() {
		return channel;
	}

	@Override
	public String getType() {
		return "changechannelvalue";
	}
}
