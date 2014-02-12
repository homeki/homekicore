package com.homeki.core.main;

import com.homeki.core.logging.L;


public abstract class ControlledThread extends Thread {
	private boolean shutdown;
	private int interval;

	public ControlledThread(int interval) {
		super();
		this.shutdown = false;
		this.interval = interval;
		this.setName(this.getClass().getSimpleName());
	}

	public void run() {
		L.i("Starting thread.");
		
		try {
			while (!shutdown) {
				iteration();
				if (interval > 0)
					Thread.sleep(interval);
			}
		} catch (InterruptedException ignore) { 
		} catch (Exception e) {
			L.e("Unhandled exception occurred.", e);
		}
		
		if (!shutdown)
			L.e("Thread was shut down due to exception.");
		else
			L.i("Thread was shut down normally.");
	}

	protected abstract void iteration() throws Exception;
	
	public void shutdown() {
		shutdown = true;
		interrupt();
	}
}
