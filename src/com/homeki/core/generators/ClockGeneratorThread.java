package com.homeki.core.generators;

import com.homeki.core.events.EventQueue;
import com.homeki.core.events.MinuteChangedEvent;
import com.homeki.core.main.ControlledThread;

public class ClockGeneratorThread extends ControlledThread {
	public ClockGeneratorThread() {
		super(60*1000);
	}

	@Override
	protected void iteration() throws Exception {
		EventQueue.getInstance().add(new MinuteChangedEvent());
	}
}
