package com.homeki.core.device.zwave;

import com.homeki.core.logging.L;
import org.zwave4j.Notification;
import org.zwave4j.NotificationWatcher;
import org.zwave4j.ValueGenre;
import org.zwave4j.ValueId;

public class ZWaveWatcher implements NotificationWatcher {
	@Override
	public void onNotification(Notification notification, Object o) {
		switch (notification.getType()) {
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
				ZWaveApi.INSTANCE.ready(notification.getHomeId());
				break;
			case NODE_NEW:
				L.i("Z-Wave new node with ID " + notification.getNodeId() + ".");
				break;
			case VALUE_ADDED:
				ValueId vid = notification.getValueId();
				if (vid.getGenre() != ValueGenre.USER) return;

				L.i("Z-Wave value added for node ID " + notification.getNodeId() + ", value type " + vid.getType().toString() + ", label " + ZWaveApi.INSTANCE.getValueLabel(vid) + ".");

				break;
		}
	}
}
