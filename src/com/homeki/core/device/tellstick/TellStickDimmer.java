package com.homeki.core.device.tellstick;

import java.util.Date;

import com.homeki.core.device.Dimmable;
import com.homeki.core.log.L;
import com.homeki.core.storage.IHistoryTable;
import com.homeki.core.storage.ITableFactory;

public class TellStickDimmer extends TellStickSwitch implements Dimmable {
	private IHistoryTable historyTable;
	
	public TellStickDimmer(String internalId, ITableFactory factory) {
		super(internalId, factory);
	}

	@Override
	public void dim(int level) {
		L.w("TellStick command not sent to dimmer, not implemented.");
		historyTable.putValue(new Date(), level);
	}
	
	@Override
	protected void ensureHistoryTable(ITableFactory factory, String tableName) {
		historyTable = factory.getHistoryTable(tableName, Integer.class);
		historyTable.ensureTable();
	}
}
