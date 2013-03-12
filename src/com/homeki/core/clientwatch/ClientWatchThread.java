package com.homeki.core.clientwatch;

import java.util.List;

import com.homeki.core.clientwatch.ClientStore.Client;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;

public class ClientWatchThread extends ControlledThread {
	public ClientWatchThread() {
		super(Configuration.CLIENT_WATCH_CHECK_INTERVAL);
	}

	@Override
	protected void iteration() throws Exception {
		List<Client> clients = ClientStore.INSTANCE.getClients();
		
		for (Client client : clients) {
			if (!client.ip.isReachable(2000))
				client.failCount++;
			
			if (client.failCount >= Configuration.CLIENT_WATCH_FAIL_COUNT_THRESHOLD)
				ClientStore.INSTANCE.removeClient(client.ip);
		}
	}
}
