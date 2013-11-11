package com.homeki.core.http.restlets.server;

import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonServerInfo;
import com.homeki.core.main.Setting;

public class ServerGetRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		String name = Setting.getString(c.ses, Setting.SERVER_NAME);
		Double longitude = Setting.getDouble(c.ses, Setting.LOCATION_LONGITUDE);
		Double latitude = Setting.getDouble(c.ses, Setting.LOCATION_LATITUDE);
		String smtpHost = Setting.getString(c.ses, Setting.SMTP_HOST);
		Integer smtpPort = Setting.getInt(c.ses, Setting.SMTP_PORT);
		Boolean smtpAuth = Setting.getBoolean(c.ses, Setting.SMTP_AUTH);
		Boolean smtpTls = Setting.getBoolean(c.ses, Setting.SMTP_TLS);
		String smtpUser = Setting.getString(c.ses, Setting.SMTP_USER);
		String smtpPassword = Setting.getString(c.ses, Setting.SMTP_PASSWORD);
		set200Response(c, gson.toJson(new JsonServerInfo(name, longitude, latitude, smtpHost, smtpPort, smtpAuth, smtpTls, smtpUser, smtpPassword)));
	}
}
