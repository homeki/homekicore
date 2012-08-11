package com.homeki.core.http.restlets.server;

import com.homeki.core.http.KiContainer;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;

public class ServerGetRestlet extends KiRestlet {
	@Override
	protected void handle(KiContainer c) {
		String name = Setting.getString(c.ses, Setting.SERVER_NAME_KEY);
		set200Response(c, gson.toJson(new JsonServerInfo(name)));
	}
}
