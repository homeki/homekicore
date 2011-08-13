package com.homekey.core.storage;

import java.util.Date;

public interface IDeviceTable {
	boolean rowExists(String internalId);
	int createRow(String internalId, String simpleName);
	int getId(String internalId);
	void setName(int id, String name);
	void setActive(int id, boolean active);
	String getName(int id);
	Date getAdded(int id);
	boolean isActive(int id);
	String getInternalId(int id);
}
