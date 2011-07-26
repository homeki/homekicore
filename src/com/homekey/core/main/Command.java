package com.homekey.core.main;

public abstract class Command<T> implements Runnable {
	private Boolean done = Boolean.FALSE;
	protected T result;
	
	public T getResult() {
		synchronized (done) {
			while (!done){
				try {
					done.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	protected void finish(){
		done = Boolean.TRUE;
		done.notifyAll();
	}
}