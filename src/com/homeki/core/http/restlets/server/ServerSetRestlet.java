package com.homeki.core.http.restlets.server;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;

public class ServerSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonServerInfo jinfo = getJsonObject(c, JsonServerInfo.class);
		
		if (jinfo.name == null || jinfo.name.length() == 0)
			throw new ApiException("Server name cannot be empty.");
		
		Setting.putString(c.ses, Setting.SERVER_NAME_KEY, jinfo.name);
		
		set200Response(c, "Server information updated successfully.");
	}
}
