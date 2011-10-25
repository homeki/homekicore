package com.homeki.core.tests;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.homeki.core.main.Util;
import com.homeki.core.storage.IDeviceTable;
import com.homeki.core.storage.ITableFactory;
import static org.junit.Assert.*;

public class SqliteStressTest {
	private IDeviceTable deviceTable;
	
	@Before
	public void setUp() throws Exception {
		ITableFactory factory = TestUtil.getEmptySqliteTestTableFactory();
		deviceTable = factory.getDeviceTable();
	}
	
	public volatile int check = 0;
	
	@Test
	public void testOneThread() {
		runStress(0, 0);
	}
	
	public class StressThread implements Runnable {
		
		private int id;
		private int rand;
		
		public StressThread(int id, int rand) {
			this.id = id;
			this.rand = rand;
		}
		
		@Override
		public void run() {
			runStress(id, rand);
		}
	}
	
	@Test
	public void testTwoThread() {
		System.out.println("Starting 2 thread test");
		check = 0;
		Random r = new Random();
		for (int i = 0; i < 2; i++) {
			StressThread s = new StressThread(0, r.nextInt());
			Thread t = new Thread(s);
			t.start();
			Util.sleep(100);
		}
		while (check < 2) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				check = 2;
				e.printStackTrace();
			}
		}
	}
	
	public void runStress(int testid, int rand) {
		System.out.println("Starting " + testid);
		String sid = "#" + testid;
		String type = new String[] { "PAPER", "SCISSOR", "ROCK" }[testid % 3];
		
		int id = -1;
		if (deviceTable.rowExists(sid)) {
			id = deviceTable.getId(sid);
		} else {
			id = deviceTable.createRow(sid, type);
		}
		
		String currentInternalId = "";
		
		// The test will fail if count+1 is co-prime to 3;
		int count = 3 * 100 + 1;
		while (count-- > 0) {
			String newInternalID = type + ":" + sid + ":" + id;
			String newName = "Name#" + count + "#" + newInternalID.hashCode();
			
			if (count % 3 == 0) {
				deviceTable.setInternalId(id, newInternalID);
				currentInternalId = newInternalID;
			}
			if (count % 5 == 0)
				deviceTable.setName(id, newName);
			assertTrue(deviceTable.rowExists(currentInternalId));
			
			if (count % 3 == 0)
				assertEquals(deviceTable.getInternalId(id), newInternalID);
			
			if (count % 5 == 0)
				assertEquals(deviceTable.getName(id), newName);
		}
		System.out.println("Stopping " + testid);
		check++;
		
	}
}
