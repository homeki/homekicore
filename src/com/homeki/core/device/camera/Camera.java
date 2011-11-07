package com.homeki.core.device.camera;

import java.lang.reflect.Type;

import com.homeki.core.device.Device;
import com.homeki.core.storage.ITableFactory;

public class Camera extends Device {

	public Camera(String internalId, String nick, ITableFactory factory) {
		super(internalId, factory);
		setName(nick);
	}

 
	@Override
	protected Type getTableValueType() {
		return Boolean.class;
	}
}
