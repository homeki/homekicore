package com.homeki.core.http.restlets.client;

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
		
		if (Util.isNullOrEmpty(jclient.id))
			throw new ApiException("Client ID cannot be null or empty.");
		
		ClientStore.INSTANCE.addClient(jclient.id);
		
		set200Response(c, msg("Client registered successfully as " + jclient.id + "."));
	}
}
