package com.homeki.core.actions;

import com.homeki.core.device.Device;
import com.homeki.core.device.Settable;
import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import javax.persistence.*;

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
			L.e("Unknown exception occurred when executing ChangeChannelValueAction on device with ID " + device.getId() + " and name '" + device.getName() + "'.", ex);
		}
	}
	
	public int getDeviceId() {
		return device.getId();
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	public Number getValue() {
		return value;
	}
	
	public void setValue(Number value) {
		this.value = value;
	}
	
	public int getChannel() {
		return channel;
	}
	
	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Override
	public String getType() {
		return "changechannelvalue";
	}
}
