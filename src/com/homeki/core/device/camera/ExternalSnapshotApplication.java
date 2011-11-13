package com.homeki.core.device.camera;

public abstract class ExternalSnapshotApplication {
	protected String tmpFolder;
	protected String sourcePath;

	public ExternalSnapshotApplication(String sourcePath, String tmpFolder){
		this.sourcePath = sourcePath;
		this.tmpFolder = tmpFolder;
	}
	
	public abstract String shoot();
}
