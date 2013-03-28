package com.homeki.core.http.restlets.server;

import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;

public class ServerGetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		String name = Setting.getString(c.ses, Setting.SERVER_NAME_KEY);
		Double longitude = Setting.getDouble(c.ses, Setting.LOCATION_LONGITUDE);
		Double latitude = Setting.getDouble(c.ses, Setting.LOCATION_LATITUDE);
		set200Response(c, gson.toJson(new JsonServerInfo(name, longitude, latitude)));
	}
}
