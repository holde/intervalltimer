package com.tobiasbrentrop.intervalltimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RunTimer extends BaseActivity implements OnClickListener {
	
	ArrayList<Integer> orderedTimesList = new ArrayList<Integer>();
	private int totalRunTime;
	private int totalTimeLeft;
	TextView textCounter;
	TextView totalCounter;
	ProgressBar progress;
	Iterator<Integer> orderedIter;
	private MediaPlayer mMediaPlayer;
	MyCounter counter;
	boolean isStart;
	
	final int BEEP = 1;
	final int COUNT = 2;
	final int BEGIN = 3;
	final int REST = 4;
	final int COMPLETE = 5;
	
	
	private static final String TAG = "IT: RunTimer";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.runtimer);
        
        View run = findViewById(R.id.runButton);
        run.setOnClickListener(this);
        View stop = findViewById(R.id.stopButton);
        stop.setOnClickListener(this);
        
        textCounter = (TextView)findViewById(R.id.runTextView);
        totalCounter = (TextView)findViewById(R.id.runTotalTimer);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.runButton:
			calcTimeList(timerList);
			totalCounter.setText(Integer.toString(totalRunTime));
			isStart = false;
			run();
			break;
		case R.id.stopButton:
			counter.cancel();
			finish();
			break;
		}
	}

	private void calcTimeList(ArrayList<int[]> timerList) {
		totalRunTime = 0;
		Iterator<int[]> it = timerList.iterator();

		while (it.hasNext()) {
			int[] times = it.next();

			totalRunTime += times[0];
			totalRunTime += (times[1]+times[3])*(times[2]-1)+times[1];
			Log.d(TAG, ""+totalRunTime);
			
			orderedTimesList.add(times[0]);
			for (int i=0;i < times[2]-1; i++) {
				orderedTimesList.add(times[1]);
				orderedTimesList.add(times[3]);
			}
			orderedTimesList.add(times[1]);
		}
		orderedIter = orderedTimesList.iterator();
		Log.d(TAG, orderedTimesList.toString());
		
		totalTimeLeft = totalRunTime;
	}
	
	private void run() {
		runNextCounter();
	}

	private void runNextCounter() {
		if(orderedIter.hasNext()) {
			int thisTime = orderedIter.next().intValue();
			counter = new MyCounter(thisTime*1000, 1000);
			textCounter.setText(thisTime+"");
			counter.start();
			
		}
	}
	
	public class MyCounter extends CountDownTimer {

		public MyCounter(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
			totalTimeLeft -= 1;
			totalCounter.setText(totalTimeLeft+"");			
			textCounter.setText("0");
			isStart = !isStart;
			if (isStart) playAudio(BEGIN);
			else playAudio(REST);			
			runNextCounter();
		}
		@Override
		public void onTick(long millisUntilFinished) {
			textCounter.setText((millisUntilFinished/1000)+"");
			totalTimeLeft -= 1;
			totalCounter.setText(totalTimeLeft+"");
			Log.d(TAG, "Total Timer: "+totalTimeLeft);
			Log.d(TAG, "Current Timer: "+(millisUntilFinished/1000));
		}
    }
	
	private void playAudio (final int sound) {
	    try {
	    	switch (sound) {
	    	case BEEP:
	    		mMediaPlayer = MediaPlayer.create(this, R.raw.beep2);
	    		break;
	    	case BEGIN:
	    		mMediaPlayer = MediaPlayer.create(this, R.raw.begin);
	    		break;
	    	case REST:
	    		mMediaPlayer = MediaPlayer.create(this, R.raw.rest);
	    		break;
	    	case COMPLETE:
	    		mMediaPlayer = MediaPlayer.create(this, R.raw.complete);
	    		break;
	    	case COUNT:
	    		mMediaPlayer = MediaPlayer.create(this, R.raw.three21);
	    		break;
	    	}
	    	
		    mMediaPlayer.setLooping(false);
		    mMediaPlayer.start();
		    mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	    		public void onCompletion(MediaPlayer arg0) {
	    			mMediaPlayer.release();
	    		}
		    });
	    } catch (Exception e) {
	    	Log.e("beep", "error: " + e.getMessage(), e);
	    }
	}
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (mMediaPlayer != null) {
		    mMediaPlayer.release();
		    mMediaPlayer = null;
	    }
	}
}
