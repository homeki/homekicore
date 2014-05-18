package com.homeki.core.events;

import com.homeki.core.device.Channel;
import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import java.util.List;

public class ChannelValueChangedEvent extends Event {
	public final int deviceId;
	public final int channel;
	public final Number value;
	
	public ChannelValueChangedEvent(int deviceId, int channel, Number value) {
		super();
		this.deviceId = deviceId;
		this.channel = channel;
		this.value = value;
	}
	
	public static void generateOnce() {
		L.i("Generating channel value changed for all channels on each device.");
		
		Session session = Hibernate.openSession();
		
		@SuppressWarnings("unchecked")
		List<Device> list = session.createCriteria(Device.class).list();
		
		for (Device d : list) {
			List<Channel> channels = d.getChannels();
			
			for (Channel c : channels) {
				HistoryPoint p = d.getLatestHistoryPoint(c.getId());
				EventQueue.INSTANCE.add(new ChannelValueChangedEvent(d.getId(), c.getId(), p.getValue()));
			}
		}
		
		Hibernate.closeSession(session);
	}
}
