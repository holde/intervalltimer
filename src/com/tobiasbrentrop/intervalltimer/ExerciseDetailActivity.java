package com.tobiasbrentrop.intervalltimer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class ExerciseDetailActivity extends BaseActivity {

	private static final String TAG = "IT: ExerciseDetailActivity";
	private Exercise currentExercise;
	private DbHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisedetail);
		final long exerciseId = getIntent().getLongExtra("exerciseId", 0);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		Log.i(TAG, "exid: "+exerciseId);
		currentExercise = new Exercise(db.selectUnitsFromExercise(exerciseId));
		setTitle(currentExercise.getExerciseTitle()+": "+currentExercise.toString());
		
		((Button)findViewById(R.id.exerciseRunBt)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent runTimer = new Intent(ExerciseDetailActivity.this, RunTimer.class);
				runTimer.putExtra("exerciseId", exerciseId);
				startActivity(runTimer);
			}
		});
		populateView();
		
	}

	
	private void populateView() {
//		((TextView)findViewById(R.id.exerciseTitleTv1)).setText(currentExercise.getExerciseTitle());
//		((TextView)findViewById(R.id.exerciseTitleTv2)).setText(currentExercise.toString());
		Log.d(TAG, "Exlist size: "+currentExercise.getExerciseCount());
		
		LinearLayout exListLl = (LinearLayout)findViewById(R.id.exerciseListLl);
		for (int i = 0; i < currentExercise.getExerciseCount(); i++) {
			
			ExerciseUnit currentUnit = currentExercise.getExerciseUnit(i);
			
		   	LinearLayout llEx1 = new LinearLayout(this);
	    	llEx1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	    	llEx1.setOrientation(LinearLayout.VERTICAL);
	    	llEx1.setId(i);
	    	exListLl.addView(llEx1);
	    	
	    	TextView tvExBig1 = new TextView(this);
	    	tvExBig1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	    	tvExBig1.setText(currentUnit.getExerciseName());
	    	tvExBig1.setTextSize(25);
	    	tvExBig1.setTextColor(0xffeebb55);
	    	llEx1.addView(tvExBig1);
	       	TextView tvExBig2 = new TextView(this);
	    	tvExBig2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
	    	tvExBig2.setText(currentUnit.toString());
	    	llEx1.addView(tvExBig2);
	    	
	    	Log.i(TAG, ""+currentUnit.toString());
	    	
	    	llEx1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent exerciseDetail = new Intent(ExerciseDetailActivity.this, ExerciseDetailActivity.class);
//					startActivity(exerciseDetail);
					Log.i(TAG, "Clicked:"+v.getId());				
				}
			});
		}
	}
}
