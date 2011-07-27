package com.homekey.core.command;

import com.homekey.core.main.Monitor;

public abstract class Command<T> implements Runnable {
	private Boolean done = Boolean.FALSE;
	protected T result;
	
	public synchronized T getResult() {
		while (!done) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@Override
	public void run() {
		internalRun();
		finish();
	}
	
	public abstract void internalRun();
	
	protected synchronized void finish() {
		done = Boolean.TRUE;
		notifyAll();
	}
	
	public T postAndWaitForResult(CommandsThread ct) {
		ct.post(this);
		return getResult();
	}
	
	public T postAndWaitForResult(Monitor m) {
		m.post(this);
		return getResult();
	}
}