package com.homekey.core.device.tellstick;

import java.util.Date;

import com.homekey.core.device.Dimmable;
import com.homekey.core.log.L;
import com.homekey.core.storage.IIntegerHistoryTable;
import com.homekey.core.storage.ITableFactory;

public class TellStickDimmer extends TellStickSwitch implements Dimmable {
	private IIntegerHistoryTable intHistoryTable;
	
	public TellStickDimmer(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	public void dim(int level) {
		L.w("TellStick command not sent to dimmer, not implemented.");
		intHistoryTable.putValue(new Date(), -1);
	}
	
	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		intHistoryTable = factory.getIntegerHistoryTable(tableName);
	}
}
