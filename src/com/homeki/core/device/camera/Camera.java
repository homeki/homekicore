package com.homeki.core.device.camera;

import java.lang.reflect.Type;

import com.homeki.core.device.Device;
import com.homeki.core.storage.ITableFactory;

public class Camera extends Device {

	public Camera(String internalId, ITableFactory factory) {
		super(internalId, factory);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Type getTableValueType() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
