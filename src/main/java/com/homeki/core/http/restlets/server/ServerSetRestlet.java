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
		
		if (Util.isNotNullAndEmpty(jinfo.name))
			throw new ApiException("Attribute 'name' exists, but is empty. An empty name is not allowed as a server name.");
		
		if (jinfo.name != null)
			Setting.putString(c.ses, Setting.SERVER_NAME, jinfo.name);
		if (jinfo.locationLongitude != null)
			Setting.putDouble(c.ses, Setting.LOCATION_LONGITUDE, jinfo.locationLongitude);
		if (jinfo.locationLatitude != null)
			Setting.putDouble(c.ses, Setting.LOCATION_LATITUDE, jinfo.locationLatitude);
		if (jinfo.smtpHost != null)
			Setting.putString(c.ses, Setting.SMTP_HOST, jinfo.smtpHost);
		if (jinfo.smtpPort != null)
			Setting.putInt(c.ses, Setting.SMTP_PORT, jinfo.smtpPort);
		if (jinfo.smtpAuth != null)
			Setting.putBoolean(c.ses, Setting.SMTP_AUTH, jinfo.smtpAuth);
		if (jinfo.smtpTls != null)
			Setting.putBoolean(c.ses, Setting.SMTP_TLS, jinfo.smtpTls);
		if (jinfo.smtpUser != null)
			Setting.putString(c.ses, Setting.SMTP_USER, jinfo.smtpUser);
		if (jinfo.smtpPassword != null)
			Setting.putString(c.ses, Setting.SMTP_PASSWORD, jinfo.smtpPassword);
		
		set200Response(c, msg("Server information updated successfully."));
	}
}
