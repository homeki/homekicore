package com.homeki.core.device;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.Session;


@Entity
public class DimmerHistoryPoint extends HistoryPoint {
	@Column(name="int_value")
	private Integer value;
	
	public Integer getValue() {
		return value;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public static List<HistoryPoint> getDimmerHistoryPoints(Session session, int deviceId, Date from, Date to) {
		@SuppressWarnings("unchecked")
		List<HistoryPoint> list = session.createQuery("from HDimmerHistoryPoint as p where p.device_id = ? and p.registered between ? and ? order by p.registered asc")
				.setInteger(0, deviceId)
				.setDate(1, from)
				.setDate(2, to)
				.list();
		return list;
	}
}
