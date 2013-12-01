package com.homeki.core.http.restlets.client;

import com.homeki.core.clientwatch.ClientStore;
import com.homeki.core.http.Container;
import com.homeki.core.http.KiRestlet;

import java.util.List;

public class ClientListRestlet extends KiRestlet {
	@Override
	protected void handle(Container c) {
		List<String> clients = ClientStore.INSTANCE.listClients();
		set200Response(c, gson.toJson(clients));
	}
}
