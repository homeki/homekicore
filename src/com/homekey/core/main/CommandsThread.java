package com.homekey.core.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CommandsThread extends Thread {
	BlockingQueue<Runnable> workQueue;

	@Override
	public void run() {
		workQueue = new LinkedBlockingQueue<Runnable>();
		while (true) {
			Runnable r = null;
			try {
				r = workQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (r != null) {
				r.run();
			}
		}
	}

	public void post(Runnable r) {
		workQueue.offer(r);
	}

}