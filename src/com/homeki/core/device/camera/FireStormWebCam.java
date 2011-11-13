package com.homeki.core.device.camera;

import java.io.File;
import java.io.IOException;

import com.homeki.core.log.L;

public class FireStormWebCam extends ExternalSnapshotApplication {
	
	public FireStormWebCam(String sourcePath, String tmpFolder) {
		super(sourcePath, tmpFolder);
	}
	
	@Override
	public String shoot() {
		String tmp = tmpFolder + "/"+ hashCode() + ".jpeg";
		File f = new File(tmp);
		if(f.exists()){
			f.delete();
		}
		String exec = String.format("fswebcam -d %s %s", sourcePath,tmp);
		try {
			Process p = Runtime.getRuntime().exec(exec);
			L.i("Waiting for fscamera..");
			p.waitFor();
			L.i("Done!");
		} catch (IOException e) {
			L.e("Could not exec '"+exec + ". " + e.getMessage());
			return null;
		} catch (InterruptedException e) {
			L.e("Could not wait for '"+exec + ". " + e.getMessage());
		}
		
		return tmp;
	}
}
