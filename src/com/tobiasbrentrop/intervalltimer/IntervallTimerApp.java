package com.tobiasbrentrop.intervalltimer;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.database.Cursor;
import android.util.Log;

public class IntervallTimerApp extends Application {
	
	private static final String TAG = "IT: App";
	
	private DbHelper db;
//	private List<Exercise> exerciseList;

	@Override
	public void onCreate() {
		super.onCreate();
		db = new DbHelper(getApplicationContext());
//		exerciseList = new ArrayList<Exercise>();
		Log.i(TAG, "onCreate");
//		getExercises();
	}	
//	public List<Exercise> getExerciseList() {
//		return exerciseList;
//	}
	
	public DbHelper getDb() {
		return db;
	}
	
//    private int getExercises() {
//    	Cursor cursor;
//		cursor = this.getDb().selectAllExercises();
//    	Exercise exercise = new Exercise(cursor);
//    	exercise.setExerciseTitle();
//    	exerciseList.add(exercise);
//    	return 1;
//    }
}
