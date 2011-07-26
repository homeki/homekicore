package com.homekey.core.main;

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
	
	protected synchronized void finish() {
		done = Boolean.TRUE;
		notifyAll();
	}
}