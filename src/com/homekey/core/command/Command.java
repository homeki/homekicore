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
		System.out.println("Finished command: " + this.toString());
	}
	
	public abstract void internalRun();
	
	protected synchronized void finish() {
		done = Boolean.TRUE;
		notifyAll();
	}
	
	@Override
	public String toString() {
		return "Command: " + getClass().toString();
	};
	
	public T postAndWaitForResult(Monitor m) {
		System.out.println("Posted command: " + this.toString());
		m.post(this);
		return getResult();
	}
}