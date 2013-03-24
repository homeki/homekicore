package com.homeki.core.http.restlets.client;

import java.net.InetAddress;

import com.homeki.core.clientwatch.ClientStore;
import com.homeki.core.http.ApiException;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;
import com.homeki.core.http.json.JsonClient;
import com.homeki.core.main.Util;

public class ClientRegisterRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		JsonClient jclient = getJsonObject(c, JsonClient.class);
		
		if (Util.isNullOrEmpty(jclient.ipAddress))
			throw new ApiException("IP address cannot be empty.");
		
		InetAddress ip = null;
		
		try {
			ip = InetAddress.getByName(jclient.ipAddress);
		} catch (Exception e) {
			throw new ApiException("Failed to parse IP address, check its format. Error message: " + e.getMessage());
		}
		
		ClientStore.INSTANCE.addClient(ip);
		
		set200Response(c, msg("Client registered successfully as " + ip.getHostAddress() + "."));
	}
}
