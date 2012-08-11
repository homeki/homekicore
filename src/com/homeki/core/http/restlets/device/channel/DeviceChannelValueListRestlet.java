package com.homeki.core.http.restlets.device.channel;

import java.util.Date;
import java.util.List;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonHistoryPoint;

public class DeviceChannelValueListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		int deviceId = getInt(c, "deviceid");
		int channelId = getInt(c, "channelid");
		Date from = getDateParam(c, "from");
		Date to = getDateParam(c, "to");
		
		Device dev = (Device)c.ses.get(Device.class, deviceId);
		
		if (dev == null)
			throw new ApiException("No device with specified ID.");
		
		@SuppressWarnings("unchecked")
		List<HistoryPoint> l = c.ses.createFilter(dev.getHistoryPoints(), "where channel = ? and registered between ? and ? order by registered desc")
				.setInteger(0, channelId)
				.setTimestamp(1, from)
				.setTimestamp(2, to)
				.list();
		
		set200Response(c, gson.toJson(JsonHistoryPoint.convertList(l)));
	}
}
