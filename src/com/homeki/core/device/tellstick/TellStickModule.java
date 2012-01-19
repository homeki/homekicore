package com.homeki.core.device.tellstick;

import java.util.ArrayList;
import java.util.Scanner;

import com.homeki.core.main.ConfigurationFile;
import com.homeki.core.main.ControlledThread;
import com.homeki.core.main.L;
import com.homeki.core.main.Module;
import com.homeki.core.main.Monitor;

public class TellStickModule implements Module {
	private ControlledThread listenerThread;
	private ControlledThread detectorThread;
	
	public TellStickModule() {

	}
	
	@Override
	public void construct(Monitor monitor, ConfigurationFile file) {
		TellStickNative.open();
		
		String idString = file.getString("module.tellstick.allowedsensors");
		ArrayList<Integer> idList = new ArrayList<Integer>();
		Scanner sc = new Scanner(idString);
		sc.useDelimiter(",");
		while (sc.hasNextInt()) {
			try {
				idList.add(sc.nextInt());
			} catch (Exception ex) {
				L.e("Error parsing configuration file value module.tellstick.allowedsensors.", ex);
				break;
			}
		}
		
		detectorThread = new TellStickDetector(10000, monitor, idList);
		detectorThread.start();
		
		listenerThread = new TellStickListener(monitor);
		listenerThread.start();
	}

	@Override
	public void destruct() {
		listenerThread.shutdown();
		detectorThread.shutdown();
		TellStickNative.close();
	}
}
