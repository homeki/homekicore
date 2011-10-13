package com.homeki.core.storage;

import java.util.Date;

public interface IDeviceTable {
	void ensureTable();
	boolean rowExists(String internalId);
	int createRow(String internalId, String type);
	int getId(String internalId);
	String getInternalId(int id);
	String getType(int id);
	String getName(int id);
	Date getAdded(int id);
	boolean isActive(int id);
	void setInternalId(int id, String name);
	void setType(int id, String type);
	void setName(int id, String name);
	void setAdded(int id, Date added);
	void setActive(int id, boolean active);
}
