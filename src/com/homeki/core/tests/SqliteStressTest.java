package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.log.L;
import com.homeki.core.main.Util;
import com.homeki.core.storage.IDeviceTable;
import com.homeki.core.storage.ITableFactory;
import static org.junit.Assert.*;

public class SqliteStressTest {
	private IDeviceTable deviceTable;
	boolean waitForEveryone;
	
	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		deviceTable = factory.getDeviceTable();
	}
	
	public volatile int check = 0;
	
	@Test
	public void testOneThread() {
		runStress(0, 0, true, true);
	}
	
	public class StressThread implements Runnable {
		
		private int id;
		private int rand;
		private boolean changeName;
		private boolean changeType;
		
		public StressThread(int id, int rand, boolean changeName, boolean changeType) {
			this.id = id;
			this.rand = rand;
			this.changeName = changeName;
			this.changeType = changeType;
		}
		
		@Override
		public void run() {
			runStress(id, rand, changeName, changeType);
		}
	}
	
	@Test
	public void testTwoThread() {
		waitForEveryone = true;
		check = 0;
		Random r = new Random();
		for (int i = 0; i < 6; i++) {
			StressThread s = new StressThread(i / 2, r.nextInt(), (i & 1) == 1, (i & 1) == 0);
			Thread t = new Thread(s);
			t.start();
			Util.sleep(100);
		}
		Util.sleep(500);
		waitForEveryone = false;
		
		while (check < 6) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				check = 6;
				e.printStackTrace();
			}
		}
	}
	
	public void runStress(int testid, int rand, boolean changeName, boolean changeType) {
		try {
			
			String sid = "#" + testid;
			String[] types = new String[] { "PAPER", "SCISSOR", "ROCK" };
			String type = types[testid % 3];
			
			int id = -1;
			if (deviceTable.rowExists(sid)) {
				id = deviceTable.getId(sid);
			} else {
				id = deviceTable.createRow(sid, type);
			}
			
			while (waitForEveryone)
				Util.sleep(1);
			Util.sleep(100);
			int count = 3 * 100 + 1;
			while (count-- > 0) {
				String newInternalID = type + ":" + sid + ":" + id;
				String newName = "Name#" + count + "#" + newInternalID.hashCode();
				type = types[count % 3];
				if (changeName)
					deviceTable.setName(id, newName);
				if (changeType)
					deviceTable.setType(id, type);
				
				if (changeType) {
					assertEquals(deviceTable.getType(id), type);
				}
				
				if (changeName)
					assertEquals(deviceTable.getName(id), newName);
			}
			check++;
			
		} catch (Exception e) {
			L.e(e.toString());
		}
	}
}
