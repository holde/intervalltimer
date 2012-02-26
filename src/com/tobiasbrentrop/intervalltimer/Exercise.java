package com.tobiasbrentrop.intervalltimer;

import java.util.ArrayList;
import java.util.List;

import com.tobiasbrentrop.intervalltimer.ExerciseUnit.Time;

import android.database.Cursor;
import android.util.Log;

public class Exercise {
	
	private static final String TAG = "IT: Exercise";
	private List<ExerciseUnit> exercises = new ArrayList<ExerciseUnit>();
	private String exerciseTitle;
	private int totalExerciseTime;
	private int totalRestTime;
	private int totalTime;
	private int[] leftTimes;
	
	public Exercise(Cursor cursor) {
		if (cursor.moveToFirst()) {
			do {
				ExerciseUnit eUnit = new ExerciseUnit(
						cursor.getString(1), cursor.getInt(2),
						cursor.getInt(3), cursor.getInt(4),
						cursor.getInt(5), cursor.getInt(6));
				exercises.add(eUnit);
				totalExerciseTime += eUnit.getTime(Time.TOTAL_WORKOUT);
				totalRestTime += eUnit.getTime(Time.TOTAL_REST);
				totalTime += eUnit.getTime(Time.TOTAL);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		leftTimes = new int[exercises.size()];
		int countTime = 0;
		for (int i = exercises.size() - 1; i >= 0; i--) {
			countTime += exercises.get(i).getTime(Time.TOTAL);
			leftTimes[i] = countTime;
		}
	}

	public void addExerciseUnit(ExerciseUnit exerciseUnit) {
		
	}
	public void setExerciseTitle() {
		this.exerciseTitle = "MYDAILYMORNING";
	}

	public String getExerciseTitle() {
		return exerciseTitle;
	}
	
	public int getExerciseCount() {
		return exercises.size();
	}
	
	public int getTotalTime() {
		return totalTime;
	}
	
	public int getWorkTime() {
		return totalExerciseTime;
	}
	
	public int getRestTime() {
		return totalRestTime;
	}
	
	public int getTimeFrom(int pos) {
		return leftTimes[pos];
	}
	
	public List<ExerciseUnit> getUnitList() {
		return exercises;
	}
	public ExerciseUnit getExerciseUnit(int location) {
		return exercises.get(location);
	}
	
	public String toString() {
		Log.i(TAG, ""+BaseActivity.timeString(this.totalTime, true));
		return ""+ exercises.size()+" exercises ("+BaseActivity.timeString(this.totalTime, true)+")";
	}
}