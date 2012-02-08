package com.tobiasbrentrop.intervalltimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class BaseActivity extends Activity {

	private static final String TAG = "IT: BaseActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		DbHelper db = ((IntervallTimerApp)getApplicationContext()).getDb();
//		Log.i(TAG, "onCreate");
//		// SYNTAX: NAME, PREP, DURATION, REPS, REST, POS, EXID
//		db.insertUnit("Knie an Brust", 30, 30, 6, 5, 1, 1);
//		db.insertUnit("Hüfte anheben1", 20, 60, 1, 0, 2, 1);
//		db.insertUnit("Hüfte anheben2", 10, 30, 1, 0, 3, 1);
//		db.insertUnit("Hüfte anheben3", 10, 15, 1, 0, 4, 1);
//		db.insertUnit("Knie seitlich", 20, 30, 6, 5, 5, 1);
//		db.insertUnit("Käfer1", 20, 60, 1, 0, 6, 1);
//		db.insertUnit("Käfer2", 20, 30, 1, 0, 7, 1);
//		db.insertUnit("Käfer3", 20, 15, 1, 0, 8, 1);
//		db.insertUnit("Band über Kopf", 40, 30, 3, 10, 9, 1);
//		db.insertUnit("Knie sitzen", 10, 30, 1, 0, 10, 1);
//		db.insertUnit("Kreuz rund", 20, 80, 1, 0, 11, 1);
//		db.insertUnit("Knie sitzen", 10, 30, 1, 0, 12, 1);
//		db.insertUnit("Kreuzen", 10, 30, 6, 10, 13, 1);
//		db.insertUnit("Knie sitzen", 10, 30, 1, 0, 14, 1);
//		db.insertUnit("Kniefall", 20, 30, 6, 10, 15, 1);
//		db.insertUnit("Band seitlich im Sitzen", 30, 15, 3, 15, 16, 1);
//		db.insertUnit("Wadeln", 20, 30, 6, 5, 17, 1);
//		db.insertUnit("Oberschenkel", 20, 30, 6, 10, 18, 1);
//		db.insertUnit("Kacken1", 30, 30, 1, 0, 19, 1);
//		db.insertUnit("Kacken2", 10, 15, 1, 0, 20, 1);
//		db.insertUnit("Kacken3", 10, 10, 1, 0, 21, 1);
//		db.insertExercise("MY DAILY MORNING", 1, "21 excercises. (00h:38m:00s)");
	}
	
	public static String timeString(int timeinSeconds) {
		int hours = timeinSeconds / 3600;
		int minutes = timeinSeconds / 60;
		int seconds = timeinSeconds % 60;
		if (hours != 0) {
			return String.format("%02dh:%02dm%02ds", hours, minutes, seconds);
		} else
			return String.format("%02dm%02ds", minutes, seconds);
	}

}
