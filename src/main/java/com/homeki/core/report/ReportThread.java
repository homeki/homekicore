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

public class ReportThread extends ControlledThread {
	private Client client;

	public ReportThread() {
		super(Configuration.REPORTER_INTERVAL);
		this.client = ClientBuilder.newClient();
	}

	@Override
	protected void iteration() {
		Session session = Hibernate.openSession();
		
		int deviceCount = ((Number)session.createCriteria(Device.class).setProjection(Projections.rowCount()).uniqueResult()).intValue();
		long historyPointCount = ((Number)session.createCriteria(HistoryPoint.class).setProjection(Projections.rowCount()).uniqueResult()).longValue();

		Report report = new Report();
		report.serverUuid = Setting.getString(session, Setting.SERVER_UUID);
		report.serverName = Setting.getString(session, Setting.SERVER_NAME);
		report.version = Util.getVersion();
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
}
