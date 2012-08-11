package com.homeki.core.http;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

import com.homeki.core.http.restlets.device.DeviceDeleteRestlet;
import com.homeki.core.http.restlets.device.DeviceGetRestlet;
import com.homeki.core.http.restlets.device.DeviceListRestlet;
import com.homeki.core.http.restlets.device.DeviceMergeRestlet;
import com.homeki.core.http.restlets.device.DeviceSetRestlet;
import com.homeki.core.http.restlets.device.channel.DeviceChannelValueListRestlet;
import com.homeki.core.http.restlets.device.channel.DeviceChannelValueSetRestlet;
import com.homeki.core.http.restlets.device.mock.DeviceMockAddRestlet;
import com.homeki.core.http.restlets.server.ServerGetRestlet;
import com.homeki.core.http.restlets.server.ServerSetRestlet;

public class RestletApplication extends Application {
	@Override
	public Restlet createInboundRoot() {
		Router r = new Router(getContext().createChildContext());
		
		r.attach("/device/list", new DeviceListRestlet());
		r.attach("/device/{deviceid}/get", new DeviceGetRestlet());
		r.attach("/device/{deviceid}/set", new DeviceSetRestlet());
		r.attach("/device/{deviceid}/merge", new DeviceMergeRestlet());
		r.attach("/device/{deviceid}/delete", new DeviceDeleteRestlet());
		
		r.attach("/device/mock/add", new DeviceMockAddRestlet());
		
		r.attach("/device/{deviceid}/channel/{channelid}/list", new DeviceChannelValueListRestlet());
		r.attach("/device/{deviceid}/channel/{channelid}/set", new DeviceChannelValueSetRestlet());
		
		r.attach("/server/get", new ServerGetRestlet());
		r.attach("/server/set", new ServerSetRestlet());
		
		return r;
	}
}
