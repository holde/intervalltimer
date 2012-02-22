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
// 		db.insertExercise("MY DAILY MORNING");		
//		db.insertUnit(new ExerciseUnit("Knie an Brust", 30, 30, 6, 5), 1);
//		db.insertUnit(new ExerciseUnit("Hüfte anheben1", 20, 60, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Hüfte anheben2", 10, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Hüfte anheben3", 10, 15, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Knie seitlich", 20, 30, 6, 5), 1);
//		db.insertUnit(new ExerciseUnit("Käfer1", 20, 60, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Käfer2", 20, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Käfer3", 20, 15, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Band über Kopf", 40, 30, 3, 10), 1);
//		db.insertUnit(new ExerciseUnit("Knie sitzen", 10, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Kreuz rund", 20, 80, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Knie sitzen", 10, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Kreuzen", 10, 30, 6, 10), 1);
//		db.insertUnit(new ExerciseUnit("Knie sitzen", 10, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Kniefall", 20, 30, 6, 10), 1);
//		db.insertUnit(new ExerciseUnit("Band seitlich im Sitzen", 30, 15, 3, 15), 1);
//		db.insertUnit(new ExerciseUnit("Wadeln", 20, 30, 6, 5), 1);
//		db.insertUnit(new ExerciseUnit("Oberschenkel", 20, 30, 6, 10), 1);
//		db.insertUnit(new ExerciseUnit("Kacken1", 30, 30, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Kacken2", 10, 15, 1, 0), 1);
//		db.insertUnit(new ExerciseUnit("Kacken3", 10, 10, 1, 0), 1);
		
	}
	
	public static String timeString(int timeinSeconds) {
		int hours = timeinSeconds / 3600;
		int minutes = timeinSeconds / 60;
		int seconds = timeinSeconds % 60;
		Log.d(TAG, "Time in seconds: "+timeinSeconds);
		if (hours != 0) {
			return String.format("%02dh:%02dm:%02ds", hours, minutes, seconds);
		} else
			return String.format("%02dm:%02ds", minutes, seconds);
	}

}
