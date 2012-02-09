package com.tobiasbrentrop.intervalltimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddUnitActivity extends Activity {
	
	private static final String TAG = "IT: AddUnitActivity";
	private DbHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addunit);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
        // on button click
		((Button)findViewById(R.id.addTimerBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// go back to list
				String name = ((EditText)findViewById(R.id.addUnitNameEt)).getText().toString();
				int prep = Integer.parseInt(((EditText)findViewById(R.id.addUnitPrepEt)).getText().toString());
				int duration = Integer.parseInt(((EditText)findViewById(R.id.addUnitDurEt)).getText().toString());
				int reps = Integer.parseInt(((EditText)findViewById(R.id.addUnitRepEt)).getText().toString());
				int rest = Integer.parseInt(((EditText)findViewById(R.id.addUnitRestEt)).getText().toString());
				if (!name.equals("")) {
					ExerciseUnit exUnit = new ExerciseUnit(name, prep, duration, reps, rest);
					db.insertUnit(exUnit, getIntent().getLongExtra("exerciseId", 0));
					Log.i(TAG, "Add New Unit: "+name);
				}
				finish();
			}
		});       		
	}
}
