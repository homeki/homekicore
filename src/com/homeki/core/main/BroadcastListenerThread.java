package com.homeki.core.main;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class BroadcastListenerThread extends ControlledThread {
	private DatagramSocket socket;
	
	public BroadcastListenerThread() throws SocketException {
		super(0);
		this.socket = new DatagramSocket(53005);
	}

	protected void iteration() throws Exception {
		try {
			byte[] buffer = new byte[2048];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			
			socket.receive(packet);
			String msg = new String(buffer, 0, packet.getLength());
			L.i("Received broadcast: " + packet.getAddress().getHostName() + ", msg: " + msg);
			packet.setLength(buffer.length);
			
			DatagramSocket replySocket = new DatagramSocket(1337);
			byte[] data = "sup".getBytes();
			DatagramPacket p = new DatagramPacket(data, data.length, packet.getAddress(), 1337);
			replySocket.send(p);
			replySocket.close();
		} catch (SocketException e) {
			L.i("Closed listener socket.");
		} catch (Exception e) {
			if (!socket.isClosed())
				socket.close();
			throw e;
		}
	}
	
	@Override
	public void shutdown() {
		socket.close();
		super.shutdown();
	}
}
