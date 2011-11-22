package com.homeki.core.threads;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.homeki.core.log.L;

public class TellstickCommandDispatcherThread extends ControlledThread {
	private static BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
	
	public TellstickCommandDispatcherThread(int interval) {
		super(interval);
	}
	
	@Override
	public void iteration() throws InterruptedException {
		Runnable r = queue.take();
		r.run();
	}
	
	public static void dim(final int level, final String internalId) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					String cmd = String.format("tdtool --dimlevel %d --dim %s", level, internalId);
					if (level == 0) {
						cmd = String.format("tdtool --off %s", internalId);
					} else if (level == 255) {
						cmd = String.format("tdtool --on %s", internalId);
					}
					
					Runtime.getRuntime().exec(cmd);
				} catch (IOException e) {
					L.e("Couldn't send command using tdtool.");
					return;
				}
			}
		};
		queue.offer(r);
	}
	
	public static void off(final String internalId) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec(String.format("tdtool -f %s", internalId));
				} catch (IOException e) {
					L.e("Couldn't send command using tdtool.");
					return;
				}
			}
		};
		queue.offer(r);
	}
	
	public static void on(final String internalId) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Runtime.getRuntime().exec(String.format("tdtool -n %s", internalId));
				} catch (IOException e) {
					L.e("Couldn't send command using tdtool.");
					return;
				}
			}
		};
		queue.offer(r);
	}
}
