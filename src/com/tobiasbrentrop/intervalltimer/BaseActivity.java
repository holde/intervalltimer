package com.tobiasbrentrop.intervalltimer;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

	private static final String TAG = "IT: BaseActivity";
	public ArrayList<int[]> timerList = new ArrayList<int[]>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Knie ran
		timerList.add(new int[] {20, 30, 6, 10});
		// Hüfte hoch
		timerList.add(new int[] {20, 60, 1, 0});
		timerList.add(new int[] {10, 30, 1, 0});
		timerList.add(new int[] {10, 15, 1, 0});
		// Knie seitlich
		timerList.add(new int[] {20, 30, 6, 5});
		// Käfer
		timerList.add(new int[] {20, 60, 1, 0});
		timerList.add(new int[] {10, 30, 1, 0});
		timerList.add(new int[] {10, 15, 1, 0});
		// Band über Kopf
		timerList.add(new int[] {20, 30, 3, 10});
		// Auf Knie
		timerList.add(new int[] {20, 30, 1, 0});
		// Rundes Kreuz
		timerList.add(new int[] {10, 60, 1, 0});
		// Knie
		timerList.add(new int[] {10, 30, 1, 0});
		// Kreuzen
		timerList.add(new int[] {10, 30, 6, 3});
		// Knie
		timerList.add(new int[] {10, 30, 1, 0});
		// Kniefall
		timerList.add(new int[] {10, 30, 6, 10});
		// Band seitlich sitzen
		timerList.add(new int[] {20, 15, 3, 10});
		// Wadeln
		timerList.add(new int[] {20, 30, 6, 5});
		// Oberschcenkel
		timerList.add(new int[] {20, 30, 6, 10});
		// Kacken
		timerList.add(new int[] {20, 20, 1, 0});
		timerList.add(new int[] {10, 10, 1, 0});
		timerList.add(new int[] {10,  5, 1, 0});
	}

	public void addTimes(int[] times) {
		timerList.add(times);
		Log.d(TAG, "addded "+Arrays.toString(times));
	}
}
