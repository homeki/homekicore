package com.homekey.core.main;

import java.util.Scanner;

import com.homekey.core.Logs;
import com.homekey.core.log.L;

public class Main {
	public static void main(String[] args) {
		L.setStandard(Logs.CORE);
		
		ThreadMaster tm = new ThreadMaster();
		
		Scanner sc = new Scanner(System.in);
		while (true) {
			try {
				String next = sc.next();
				L.i("Got command: " + next);
				if (next.equals("restart")) {
					tm.restart();
				}
				if (next.equals("exit")) {
					tm.shutdown();
					break;
				}
			} catch (Exception e) {
				// just ignore the exception here,
				// scanner throws a NoSuchElementException
				// when terminating otherwise
			}
		}
	}
}
