package com.homeki.core.threads;

import com.homeki.core.log.L;

public abstract class ControlledThread extends Thread {
	
	private boolean shutdown;
	private int interval;
	private boolean quiet;
	
	public ControlledThread(int interval) {
		super();
		this.shutdown = false;
		this.quiet = false;
		this.interval = interval;
		setName(this.getClass().getSimpleName());
	}
	
	protected boolean keepRunning() {
		return !shutdown;
	}
	
	public void quiet() {
		this.quiet = true;
	}
	
	public void run() {
		if (!quiet)
			L.i("Starting thread.");
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
			if (!quiet)
			L.i("Thread was shut down.");
		}
	}
	
	public abstract void iteration() throws InterruptedException;
	
	public void shutdown() {
		shutdown = true;
		interrupt();
	}
}
