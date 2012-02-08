package com.tobiasbrentrop.intervalltimer;

import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class RunTimer extends BaseActivity {
	
	private static final String TAG = "IT: RunTimer";
	private DbHelper db;
	
	// exercise
	private Exercise currentExercise;
	private ExerciseUnit currentUnit;
	private int unitProgress = 0;
	private int currUnitId = 0;
	private int prevUnitId = -1;
	private int nextUnitId = 1;
	
	// for sound
	private SoundPool soundPool;
	private boolean loaded = false;
	private static int BEEP;
	private static int COUNT;
	private static int BEGIN;
	private static int REST;
	private static int COMPLETE;
	private static int NEXT;
	
	// timer state
	private CountDownTimer cdTimer;
	int totalTimeLeft;
	int exTimeLeft;
	int cycle;
	
	// views
	
	TextView timerType;
	TextView timerTotalTimeLeft;
	TextView timerCurrentTimeLeft;
	TextView timerCycle;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtimer);
		loadSounds();
		final long exerciseId = getIntent().getLongExtra("exerciseId", 0);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		currentExercise = new Exercise(db.selectUnitsFromExercise(exerciseId));
		currentUnit = currentExercise.getExerciseUnit(currUnitId);
        setTitle(currentExercise.getExerciseTitle()+": "+currentExercise.toString());
     	timerType = (TextView)findViewById(R.id.runTimerTypeTv);
    	timerTotalTimeLeft = (TextView)findViewById(R.id.runTimerTotalTimeLeftTv);
    	timerCurrentTimeLeft = (TextView)findViewById(R.id.runTimerCurrentTimeLeftTv);
    	timerCycle = (TextView)findViewById(R.id.runTimerCycle);
    	
    	totalTimeLeft = currentExercise.getTotalTime();
    	cycle = 1;
    	
        registerEvents();
		populateView();
		exTimeLeft = currentUnit.getTimesArray()[unitProgress];
		startTimer();
    }

	
	private void registerEvents() {
		((Button)findViewById(R.id.nextBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				nextUnit();
			}
		});
		((Button)findViewById(R.id.prevBtn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				prevUnit();
			}
		});
		final Button pauseBtn = (Button)findViewById(R.id.runPauseBt);
		pauseBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pauseBtn.getText().equals("PAUSE")) {
					pauseBtn.setText("RESUME");
					cdTimer.cancel();
				} else {
					startTimer();
					pauseBtn.setText("PAUSE");
				}				
			}
		});		
	}

	private void populateView() {
//		((TextView)findViewById(R.id.exerciseTitleTv1)).setText(currentExercise.getExerciseTitle());
//		((TextView)findViewById(R.id.exerciseTitleTv2)).setText(currentExercise.toString());
		updateExerciseUnits();
	}
	
	private void updateExerciseUnits() {
		if (prevUnitId >= 0) {
			((TextView)findViewById(R.id.prevExTv)).setText(currentExercise.getExerciseUnit(prevUnitId).getExerciseName());
		} else {
			((TextView)findViewById(R.id.prevExTv)).setText("");
		}
		((TextView)findViewById(R.id.currExTv)).setText(currentUnit.getExerciseName());
		if (nextUnitId < currentExercise.getExerciseCount()) {
			((TextView)findViewById(R.id.nextExTv)).setText(currentExercise.getExerciseUnit(nextUnitId).getExerciseName());
		} else {
			((TextView)findViewById(R.id.nextExTv)).setText("");
		}
	}
	
	private void nextUnit() {
		if (currUnitId < currentExercise.getExerciseCount() - 1) {
			prevUnitId++;
			currUnitId++;
			nextUnitId++;
			currentUnit = currentExercise.getExerciseUnit(currUnitId);
			unitProgress = 0;
			exTimeLeft = currentUnit.getTimesArray()[unitProgress];
			totalTimeLeft = currentExercise.getTimeFrom(currUnitId);
			cycle = 0;
			updateExerciseUnits();
			cdTimer.cancel();
			startTimer();
		}
	}

	private void prevUnit() {
		if (currUnitId > 0) {
			currUnitId--;
			nextUnitId--;
			prevUnitId--;
			currentUnit = currentExercise.getExerciseUnit(currUnitId);
			unitProgress = 0;
			exTimeLeft = currentUnit.getTimesArray()[unitProgress];
			totalTimeLeft = currentExercise.getTimeFrom(currUnitId);
			cycle = 0;
			updateExerciseUnits();
			cdTimer.cancel();
			startTimer();
		}
	}
	
	private void startTimer() {
		Log.i(TAG, "Ex:"+currentUnit.getExerciseName()+" -- Unit:"+currUnitId+" -- Progress:"+unitProgress+" -- UnitTime:"+exTimeLeft);
		if (exTimeLeft == 0) {
            if (unitProgress < currentUnit.getCount() - 1) {
            	unitProgress++;
            	exTimeLeft = currentUnit.getTimesArray()[unitProgress];
            	startTimer();
            } else {
            	if (currUnitId < currentExercise.getExerciseCount() - 1) {
            		nextUnit();
            	} else {
            		onComplete();
            	}
            }
		} else {
			if (unitProgress == 0) {
				timerCycle.setText(""+cycle+" / "+currentUnit.getRepetitions());
				timerType.setText("PREPARE");
				play(NEXT);
			} else if (unitProgress % 2 == 1) {
				cycle++;
				timerCycle.setText(""+cycle+" / "+currentUnit.getRepetitions());
				timerType.setText("EXERCISE");
				play(BEGIN);
			} else {
				timerType.setText("REST");
				play(REST);
			}
			cdTimer = new CountDownTimer(exTimeLeft * 1000, 1000) {
				public void onTick(long millisUntilFinished) {
					totalTimeLeft -= 1;
					exTimeLeft -= 1;
					timerTotalTimeLeft.setText(BaseActivity.timeString(totalTimeLeft));
		            timerCurrentTimeLeft.setText(BaseActivity.timeString((int)millisUntilFinished / 1000));
		        	if (exTimeLeft == 5) {
		        		play(COUNT);
		        	}
		        }
		        public void onFinish() {
		        	totalTimeLeft--;
		        	timerTotalTimeLeft.setText(BaseActivity.timeString(totalTimeLeft));
		        	timerCurrentTimeLeft.setText(BaseActivity.timeString(0));
		            if (unitProgress < currentUnit.getCount() - 1) {
		            	unitProgress++;
		        		exTimeLeft = currentUnit.getTimesArray()[unitProgress];
		            	startTimer();
		            } else {
		            	if (currUnitId < currentExercise.getExerciseCount() - 1) {
		            		nextUnit();
		            	} else {
		            		onComplete();
		            	}
		            }
		        }
	        }.start();
		}
	}
	
	private void loadSounds() {
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				if (sampleId == 6) {
					loaded = true;
				}
			}
		});
		BEEP = soundPool.load(this, R.raw.beep, 1);
		COUNT = soundPool.load(this, R.raw.three21, 2);
		BEGIN = soundPool.load(this, R.raw.begin, 3);
		REST = soundPool.load(this, R.raw.rest, 4);
		COMPLETE = soundPool.load(this, R.raw.complete, 5);
		NEXT = soundPool.load(this, R.raw.next, 6);
	}

	private void play(int soundID) {
		// Is the sound loaded already?
		if (loaded) {
			soundPool.play(soundID, 0.7f, 0.7f, 1, 0, 1f);
		}
	}
	
	private void onComplete() {
		play(COMPLETE);
		timerType.setText("COMPLETE");
	}
	
	protected void onPause() {
		cdTimer.cancel();
		this.finish();
		super.onPause();
	}

	protected void onDestroy() {
	    super.onDestroy();
	}
}
