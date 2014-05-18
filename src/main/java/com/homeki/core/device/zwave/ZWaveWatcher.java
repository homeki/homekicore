package com.homeki.core.device.zwave;

import com.homeki.core.device.Device;
import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.zwave4j.Notification;
import org.zwave4j.NotificationWatcher;
import org.zwave4j.ValueGenre;
import org.zwave4j.ValueId;

public class ZWaveWatcher implements NotificationWatcher {
	@Override
	public void onNotification(Notification notf, Object o) {
		Thread.currentThread().setName("ZWaveWatcher");
		Session session = Hibernate.openSession();

		try {
			switch (notf.getType()) {
				case DRIVER_READY:
					L.i("Z-Wave controller ready.");
					break;
				case DRIVER_FAILED:
					L.e("Z-Wave controller failed.");
					break;
				case DRIVER_RESET:
					L.i("Z-Wave controller reset.");
					break;
				case ALL_NODES_QUERIED:
				case ALL_NODES_QUERIED_SOME_DEAD:
					L.i("Z-Wave nodes queried.");
					ZWaveApi.INSTANCE.ready(notf.getHomeId());
					break;
				case NODE_ADDED:
					nodeAdded(session, notf.getNodeId());
					break;
				case NODE_REMOVED:
					nodeRemoved(session, notf.getNodeId());
					break;
				case VALUE_ADDED:
					valueAdded(session, notf.getValueId());
					break;
				case VALUE_CHANGED:
				case VALUE_REFRESHED:
					valueChanged(session, notf.getValueId());
					break;
			}
		} finally {
			Hibernate.closeSession(session);
		}
	}

	private void valueAdded(Session session, ValueId vid) {
		if (vid.getGenre() != ValueGenre.USER) {
			L.i("Z-Wave value added for node with internal ID zw-" + vid.getNodeId() + ", but not a USER value so ignored.");
			return;
		}
		if (ZWaveApi.convertValueType(vid.getType()) == null) {
			L.i("Z-Wave value added for node with internal ID zw-" + vid.getNodeId() + ", but value was of unsupported type " + vid.getType().name() + " so ignored.");
			return;
		}

		Device dev = Device.getByInternalId("zw-" + vid.getNodeId());

		if (dev == null) {
			L.i("Z-Wave value added for node with internal ID zw-" + vid.getNodeId() + " not existing, ignored.");
			return;
		}

		ZWaveChannel channel = ZWaveChannel.getByDeviceAndIndex(dev, vid.getIndex());

		if (channel != null) {
			L.i("Z-Wave value added for device with ID " + dev.getId() + ", but channel already existed.");
			return;
		}

		channel = new ZWaveChannel();
		channel.setDevice(dev);
		channel.setCommandClassId(vid.getCommandClassId());
		channel.setIndex(vid.getIndex());
		channel.setInstance(vid.getInstance());
		channel.setName(ZWaveApi.INSTANCE.getValueLabel(vid));
		channel.setValueType(vid.getType());
		channel.setDataType(ZWaveApi.convertValueType(vid.getType()));
		channel.setGenre(vid.getGenre());
		session.save(channel);

		valueChanged(session, vid);

		L.i("Z-Wave value added for device with ID " + dev.getId() + ", created new channel with name " + channel.getName() + ".");
	}

	private void valueChanged(Session session, ValueId vid) {
		Device dev = Device.getByInternalId("zw-" + vid.getNodeId());

		if (dev == null) {
			L.i("Z-Wave value changed for node with internal ID zw-" + vid.getNodeId() + ", device did not exist, ignored.");
			return;
		}

		ZWaveChannel channel = ZWaveChannel.getByDeviceAndIndex(dev, vid.getIndex());

		if (channel == null) {
			L.i("Z-Wave value changed for device with ID " + dev.getId() + ", but channel did not exist, ignored.");
			return;
		}

		switch (channel.getDataType()) {
			case INT:
				int intValue = ZWaveApi.INSTANCE.getIntValue(vid);
				dev.addHistoryPoint(channel.getId(), intValue);
				L.i("Z-Wave channel " + channel.getName() + " changed to " + intValue + ".");
				break;
			case BOOL:
				int boolValue = ZWaveApi.INSTANCE.getBoolValue(vid) ? 1 : 0;
				dev.addHistoryPoint(channel.getId(), boolValue);
				L.i("Z-Wave channel " + channel.getName() + " changed to " + boolValue + ".");
				break;
			case DOUBLE:
				double doubleValue = ZWaveApi.INSTANCE.getDoubleValue(vid);
				dev.addHistoryPoint(channel.getId(), doubleValue);
				L.i("Z-Wave channel " + channel.getName() + " changed to " + doubleValue + ".");
				break;
			default:
				L.i("Z-Wave channel " + channel.getName() + " received value but unknown data type, ignored.");
		}

		session.save(dev);
	}

	private void nodeRemoved(Session session, short nodeId) {
		Device dev = Device.getByInternalId("zw-" + nodeId);

		if (dev == null) {
			L.i("Z-Wave removed node with internal ID zw-" + nodeId + ", but no such device existed.");
			return;
		}

		dev.setActive(false);
		session.save(dev);
		L.i("Z-Wave removed node with internal ID zw-" + nodeId + ", device set to inactive.");
	}

	private void nodeAdded(Session session, short nodeId) {
		Device dev = Device.getByInternalId("zw-" + nodeId);

		if (dev != null && dev.isActive()) {
				L.i("Z-Wave new/added node with internal ID zw-" + nodeId + " already exists and is active, no action taken.");
		} else if (dev != null && !dev.isActive()) {
			dev.setActive(true);
			session.save(dev);
			L.i("Z-Wave new/added node with internal ID zw-" + nodeId + " already exists but is not active, updated to active.");
		} else {
			dev = new ZWaveDevice();
			dev.setInternalId("zw-" + nodeId);
			session.save(dev);
			L.i("Z-Wave new/added node with internal ID zw-" + nodeId + " not found, created new device.");
		}
	}
}
