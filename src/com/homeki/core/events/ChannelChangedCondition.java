package com.homeki.core.events;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ChannelChangedCondition extends Condition {
	@Column
	private int deviceId;
	
	@Column
	private int channel;
	
	@Column
	private Number value;
	
	@Column
	private int operator;
	
	public boolean check(Event e) {
		if (e instanceof ChannelChangedEvent) {
			ChannelChangedEvent cce = (ChannelChangedEvent) e;
			if (cce.channel == this.channel && cce.deviceId == this.deviceId) {
				int v = Double.compare(cce.value.doubleValue(), value.doubleValue());
				switch (operator) {
				case EQ:
					return v == 0;
				case LT:
					return v < 0;
				case GT:
					return v > 0;
				}
			}
		}
		return false;
	}
}
