package com.homekey.core.main;

import com.homekey.core.log.L;

public abstract class ControlledThread extends Thread {
	
	private boolean shutdown;
	
	public ControlledThread() {
		super();
		this.shutdown = false;
	}
	
	protected boolean keepRunning() {
		return !shutdown;
	}
	
	public void run() {
		try {
			while (!shutdown) {
				internalLoop();
			}
		} catch (InterruptedException e) {}
		if(!shutdown){
			L.e("Thread exited without permission.");
		}else{
			L.i("Thread was shut down.");
		}
	}
	
	public abstract void internalLoop() throws InterruptedException;
	
	public void shutdown() {
		shutdown = true;
		interrupt();
	}
}
