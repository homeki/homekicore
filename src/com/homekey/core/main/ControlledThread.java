package com.homekey.core.main;

import com.homekey.core.log.L;

public abstract class ControlledThread extends Thread {
	
	private boolean shutdown;
	private int interval;
	
	public ControlledThread(int interval) {
		super();
		this.shutdown = false;
		this.interval = interval;
	}
	
	protected boolean keepRunning() {
		return !shutdown;
	}
	
	public void run() {
		try {
			while (!shutdown) {
				iteration();
				if (interval > 0)
					Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			
		}
		if (!shutdown) {
			L.e("Thread exited without permission.");
		} else {
			L.i("Thread was shut down.");
		}
	}
	
	public abstract void iteration() throws InterruptedException;
	
	public void shutdown() {
		shutdown = true;
		interrupt();
	}
}
