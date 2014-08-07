package com.homeki.core.main;

import com.homeki.core.logging.L;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

// Protocol
// ===============================
// Just sends a simple string, values seperated by |. Triggered by UDP packet broadcasted to port 53005.
//
// Request: 
// "HOMEKI"
//
// Response:
// "[HOMEKI_SERVER_NAME]|[HOMEKI_SERVER_HOSTNAME]"
//
public class BroadcastListenerThread extends ControlledThread {
	private DatagramSocket socket;
	
	public BroadcastListenerThread() throws SocketException {
		super(0);
		this.socket = new DatagramSocket(53005);
	}

	protected void iteration() throws Exception {
		try {
			byte[] buffer = new byte[512];
			DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
			socket.receive(requestPacket);
			
			String msg = new String(buffer, 0, requestPacket.getLength());
			
			if (msg.startsWith("HOMEKI")) {
				L.i("Received broadcast from " + requestPacket.getAddress().getHostAddress() + ".");
				
				Session session = Hibernate.openSession();
				String response = Setting.getString(session, Setting.SERVER_NAME) + "|" + Setting.getString(session, Setting.SERVER_HOSTNAME);
				Hibernate.closeSession(session);
				
				byte[] data = response.getBytes();
				DatagramPacket responsePacket = new DatagramPacket(data, data.length, requestPacket.getAddress(), 1337);
				socket.send(responsePacket);
			} else {
				L.w("Received invalid broadcast request from " + requestPacket.getAddress().getHostAddress() + ", ignored.");
			}
		} catch (SocketException e) {
			L.i("Closed broadcast listener socket.");
		} catch (Exception e) {
			if (!socket.isClosed())
				socket.close();
			throw e;
		}
	}
	
	@Override
	public void shutdown() {
		super.shutdown();
		socket.close();
	}
}
