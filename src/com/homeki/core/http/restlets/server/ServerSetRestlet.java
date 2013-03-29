package com.homeki.core.http.restlets.server;

import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;
import com.homeki.core.main.Util;

public class ServerSetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonServerInfo jinfo = getJsonObject(c, JsonServerInfo.class);
		
		if (Util.isNullOrEmpty(jinfo.name))
			throw new ApiException("Server name cannot be empty.");
		
		Setting.putString(c.ses, Setting.SERVER_NAME, jinfo.name);
		Setting.putDouble(c.ses, Setting.LOCATION_LONGITUDE, jinfo.locationLongitude);
		Setting.putDouble(c.ses, Setting.LOCATION_LATITUDE, jinfo.locationLatitude);
		Setting.putString(c.ses, Setting.SMTP_HOST, jinfo.smtpHost);
		Setting.putInt(c.ses, Setting.SMTP_PORT, jinfo.smtpPort);
		Setting.putBoolean(c.ses, Setting.SMTP_AUTH, jinfo.smtpAuth);
		Setting.putBoolean(c.ses, Setting.SMTP_TLS, jinfo.smtpTls);
		Setting.putString(c.ses, Setting.SMTP_USER, jinfo.smtpUser);
		Setting.putString(c.ses, Setting.SMTP_PASSWORD, jinfo.smtpPassword);
		
		set200Response(c, msg("Server information updated successfully."));
	}
}
