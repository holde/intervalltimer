package com.tobiasbrentrop.intervalltimer;

public class ExerciseUnit {

	// enum may be cleaner, but too complex
	public final static class Time {
		final static int PREPARATION = 0;
		final static int WORKOUT = 1;
		final static int REPETITIONS = 2;
		final static int REST = 3;
		final static int COOL_DOWN = 4;
		final static int TOTAL = 5;
		final static int TOTAL_REST = 6;
		final static int TOTAL_WORKOUT = 7;
	}
	
	public static enum Mode {
		IDLE,
		PREPARATION,
		WORKOUT,
		REST,
		COOLDOWN
	}

	// state
	private String mName;					// name of the unit 
	private int[] mTimes = new int[8];		// 
	private int[] mPeriods;
	
	/**
	 * Constructor
	 * @param name Name of the exercise-unit
	 * @param preparationTime Time for preparation
	 * @param workoutTime Time for workout
	 * @param repetitions Number of cycles
	 * @param restTime Time for rest between workouts
	 * @param coolTime Time for rest at the end of unit
	 */
	public ExerciseUnit(String name, int preparationTime, int workoutTime, int repetitions, int restTime, int coolTime) {
		mName = name;
		mTimes[Time.PREPARATION] = preparationTime;
		mTimes[Time.WORKOUT] = workoutTime;
		mTimes[Time.REPETITIONS] = repetitions;
		mTimes[Time.REST] = restTime;
		mTimes[Time.COOL_DOWN] = coolTime;
		calculateTimes();
	}
	
	private void calculateTimes() {
		if (mTimes[Time.REPETITIONS] != 0) {
			mTimes[Time.TOTAL_REST] = mTimes[Time.REST] *
					(mTimes[Time.REPETITIONS] - 1) +
					mTimes[Time.COOL_DOWN];
			mTimes[Time.TOTAL_WORKOUT] = mTimes[Time.REPETITIONS] * mTimes[Time.WORKOUT];
			mTimes[Time.TOTAL] = mTimes[Time.TOTAL_REST] + mTimes[Time.TOTAL_WORKOUT];
			mPeriods = new int[mTimes[Time.REPETITIONS] * 2 + 1];
		} else {
			mTimes[Time.TOTAL_REST] = mTimes[Time.COOL_DOWN];
			mTimes[Time.TOTAL_WORKOUT] = 0;
			mTimes[Time.TOTAL] = mTimes[Time.TOTAL_REST];
			mPeriods = new int[2];
		}
		mPeriods[0] = mTimes[Time.PREPARATION];
		for (int i = 0; i < mTimes[Time.REPETITIONS] * 2; i += 2) {
			mPeriods[i+1] = mTimes[Time.WORKOUT];
			mPeriods[i+2] = mTimes[Time.REST];
		}
		mPeriods[mPeriods.length - 1] = mTimes[Time.COOL_DOWN];
	}
	
	public String getName() {
		return mName;
	}
	
	public int getTime(int time) {
		return mTimes[time];
	}
	
	public String getInfo() {
		return ""+mTimes[Time.PREPARATION]+"s prep | " +
				mTimes[Time.REPETITIONS]+"*"+mTimes[Time.WORKOUT]+"s work | " +
				mTimes[Time.REST]+"s rest (" +
				BaseActivity.timeString(mTimes[Time.TOTAL])+")";
	}

	public int[] getTimesArray() {
		return mPeriods;
	}
	
	public int getCount() {
		return mPeriods.length;
	}
	
}
