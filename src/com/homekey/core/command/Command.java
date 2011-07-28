package com.homekey.core.command;

import com.homekey.core.main.InternalData;

public abstract class Command<T> {
	private Boolean done = Boolean.FALSE;
	private T result;
	
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
	
	public void run(InternalData data) {
		internalRun(data);
		finish();
		System.out.println("Finished command: " + this.toString());
	}
	
	protected synchronized void setResult(T result){
		this.result = result;
	}
	
	public abstract void internalRun(InternalData data);
	
	protected synchronized void finish() {
		done = Boolean.TRUE;
		notifyAll();
	}
	
	@Override
	public String toString() {
		return "Command: " + getClass().toString();
	};
	
	public T postAndWaitForResult(CommandQueue m) {
		System.out.println("Posted command: " + this.toString());
		m.post(this);
		return getResult();
	}
}