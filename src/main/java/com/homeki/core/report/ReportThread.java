package com.homeki.core.report;

import com.homeki.core.device.Device;
import com.homeki.core.device.HistoryPoint;
import com.homeki.core.logging.L;
import com.homeki.core.main.Configuration;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.Setting;
import com.homeki.core.main.Util;
import com.homeki.core.storage.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.NoSuchElementException;

public class ReportThread extends ControlledThread {
	private String macAddress;
	private Client client;

	public ReportThread() {
		super(Configuration.REPORTER_INTERVAL);
		this.macAddress = getMacAddress();
		this.client = ClientBuilder.newClient();
	}

	@Override
	protected void iteration() {
		Session session = Hibernate.openSession();
		
		int deviceCount = ((Number)session.createCriteria(Device.class).setProjection(Projections.rowCount()).uniqueResult()).intValue();
		long historyPointCount = ((Number)session.createCriteria(HistoryPoint.class).setProjection(Projections.rowCount()).uniqueResult()).longValue();
		String serverName = Setting.getString(session, Setting.SERVER_NAME);
		
		Report report = new Report();
		report.macAddress = macAddress;
		report.version = Util.getVersion();
		report.serverName = serverName;
		report.deviceCount = deviceCount;
		report.historyPointRowCount = historyPointCount;
		
		Hibernate.closeSession(session);
		
		try {
			Response response = client
														.target(Configuration.REPORTER_URL)
														.request(MediaType.APPLICATION_JSON)
														.put(Entity.json(report));

			if (response.getStatus() == 200)
				L.i("Instance status was successfully reported.");
			else
				L.e("Failed to report instance status, HTTP status code was " + response.getStatus() + ".");
		} catch (Exception e) {
			L.e("Failed to report instance status.", e);
		}
	}

	private String getMacAddress() {
		try {
			NetworkInterface netInt = null;
			
			for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if (ni.isLoopback())
					continue;
				
				netInt = ni;
				break;
			}
			
			if (netInt == null)
				throw new NoSuchElementException("Failed to find any network interface to fetch MAC address from.");
			
			StringBuilder sb = new StringBuilder();
			for(byte b : netInt.getHardwareAddress())
				sb.append(String.format("%02x:", b&0xff));
			sb.deleteCharAt(sb.length()-1);
			
			return sb.toString();
		} catch (SocketException e) {
			throw new NoSuchElementException("Failed to find any network interface to fetch MAC address from (socket error).");
		}
	}
}
