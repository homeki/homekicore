package com.homeki.core.device.camera;

import java.io.File;
import java.lang.reflect.Type;

import com.homeki.core.device.Device;
import com.homeki.core.log.L;
import com.homeki.core.storage.ITableFactory;

public class Camera extends Device {
	
	private ExternalSnapshotApplication snapshooter;
	
	public Camera(String internalId, ITableFactory factory) {
		super(internalId, factory);
		setName(internalId);
		setActive(false);
		snapshooter = new FireStormWebCam(internalId, makeTemporaryPath());
		
	}
	
	public synchronized String snapshot() {
		long t = -System.currentTimeMillis();
		String path = snapshooter.shoot();
		t +=System.currentTimeMillis();
		L.i("Took screen shot in " + t + " ms.");
		return path;
	}
	
	private String makeTemporaryPath() {
		String path = "/tmp/video" + Math.abs(getInternalId().hashCode());
		File f = new File(path);
		L.i(f.getAbsolutePath());
		if (f.mkdir()) {
			L.i("Created tmp path: " + path);
			return path;
		} else if (f.exists()) {
			L.i("Path already existed: " + path);
			return path;
		} else {
			L.w("Could not create tmp path: " + path);
			return null;
		}
		
	}
	
	@Override
	public void setActive(boolean active) {
		if (isActive()) {
			
		} else {
			
		}
	}
	
	@Override
	protected Type getTableValueType() {
		return Boolean.class;
	}
}
