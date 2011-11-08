package com.homeki.core.device.camera;

import java.lang.reflect.Type;

import com.homeki.core.device.Device;
import com.homeki.core.storage.ITableFactory;

public class Camera extends Device {
	public Camera(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}
 
	@Override
	protected Type getTableValueType() {
		return Boolean.class;
	}
}
