package com.homeki.core.threads.mock;

import com.homeki.core.main.ControlledThread;

public class ControlledMockThread extends ControlledThread {

	private int k;

	public ControlledMockThread() {
		super(100);
		k = 0;
	}

	@Override
	public void iteration() throws InterruptedException {
		if(k++ == 10){
			shutdown();
		}
	}
}
