package com.homekey.core.main;

import com.homekey.core.command.CommandQueue;
import com.homekey.core.command.GetLoggableDevicesCommand;
import com.homekey.core.device.Device;
import com.homekey.core.device.Renewable;
import com.homekey.core.storage.Database;

public class CollectorThread extends Thread {
	private CommandQueue queue;
	private Database db;
	
	public CollectorThread(CommandQueue queue, Database db) {
		this.queue = queue;
		this.db = db;
	}
	
	@Override
	public void run() {
		while (true) {
			Device[] loggableDevices = new GetLoggableDevicesCommand().postAndWaitForResult(queue);
			
			for (Device d : loggableDevices) {
				Renewable<?> r = (Renewable<?>)d;
				d.setValue(r.getNewValue());
			}
			
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				System.out.println("CollectorThread interrupted.");
				return;
			}
		}
	}
}
