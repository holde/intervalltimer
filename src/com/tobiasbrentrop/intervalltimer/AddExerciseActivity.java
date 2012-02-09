package com.tobiasbrentrop.intervalltimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddExerciseActivity extends Activity {
	
	private static final String TAG = "IT: AddExerciseActivity";
	private DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addexercise);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
        // on button click
		((Button)findViewById(R.id.addTimerBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// go back to list
				String name = ((EditText)findViewById(R.id.addTimerEt)).getText().toString();
				if (!name.equals("")) {
					db.addNewExercise(name);
					Log.i(TAG, "Add New exercise: "+name);
				}
				finish();
			}
		});       		
	}
}
