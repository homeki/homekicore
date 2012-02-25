package com.homeki.core.main;


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
		} catch (InterruptedException ignore) { 
		} catch (Exception e) {
			L.e("Unhandled exception occured.", e);
		}
		
		if (!shutdown)
			L.e("Thread was shut down due to exception.");
		else if (!quiet)
			L.i("Thread was shut down normally.");
	}

	protected abstract void iteration() throws Exception;
	
	public void shutdown() {
		shutdown = true;
		interrupt();
	}
}
