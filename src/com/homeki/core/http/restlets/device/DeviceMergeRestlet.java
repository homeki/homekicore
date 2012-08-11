package com.homeki.core.http.restlets.device;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;

public class DeviceMergeRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int deviceId = getInt(c, "deviceid");
		int mergeWithDeviceId = getIntParam(c, "mergewith");
		
		Device dev = (Device)c.ses.get(Device.class, deviceId);
		Device mergeWithDev = (Device)c.ses.get(Device.class, mergeWithDeviceId);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		if (mergeWithDev == null)
			throw new ApiException("No merge-with device with specified ID.");
		
		if (!dev.getType().equals(mergeWithDev.getType()))
			throw new ApiException("Device type ('" + dev.getType() + "') has to be the same as merge-with device type ('" + mergeWithDev.getType() + "'.");
		
		// we do this first, cause if this fails we don't want to move the history points
		mergeWithDev.preDelete();
		
		// nope, addAll on the list does not work
		for (HistoryPoint p : mergeWithDev.getHistoryPoints()) {
			p.setDevice(dev);
			dev.getHistoryPoints().add(p);
		}
		mergeWithDev.getHistoryPoints().clear();
		
		c.ses.delete(mergeWithDev);
		
		set200Response(c, "Merge-with device successfully merged into device.");
	}
}
