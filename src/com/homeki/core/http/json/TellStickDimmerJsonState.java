package com.homeki.core.http.json;

import com.homeki.core.device.IntegerHistoryPoint;
import com.homeki.core.device.tellstick.TellStickDimmer;

public class TellStickDimmerJsonState extends JsonState {
	public Integer value;
	public Integer level;
	
	public TellStickDimmerJsonState(TellStickDimmer d) {
		IntegerHistoryPoint onoff = (IntegerHistoryPoint)d.getLatestHistoryPoint(TellStickDimmer.TELLSTICKDIMMER_ONOFF_CHANNEL);
		IntegerHistoryPoint level = (IntegerHistoryPoint)d.getLatestHistoryPoint(TellStickDimmer.TELLSTICKDIMMER_LEVEL_CHANNEL);
		this.value = onoff.getValue();
		this.level = level.getValue();
	}
}
