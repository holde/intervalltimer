package com.tobiasbrentrop.intervalltimer;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddExerciseActivity extends Activity {
	
	private static final String TAG = "IT: AddExerciseActivity";
	private DbHelper db;
	private boolean edit;
	private String exName;
	
	private EditText et;
	
	private final TextWatcher UPDATE_TITLE = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			setTitle(et.getText().toString());				
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addexercise);
		final long exerciseId = getIntent().getLongExtra("exerciseId", 0);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		// update title
		et = (EditText)findViewById(R.id.add_dialog_name);
		et.addTextChangedListener(UPDATE_TITLE);
		// new exercise
		if (exerciseId == 0) { 
			edit = false;
			setTitle("New Exercise");
		// edit exercise
		} else {
			edit = true;
			exName = db.getExerciseName(exerciseId);
			setTitle(exName);
			((Button)findViewById(R.id.add_dialog_ok_btn)).setText("Edit");
			et.setText(exName);
			// cursor to end
			et.setSelection(et.getText().length());
			
		}
		// ok button click
		((Button)findViewById(R.id.add_dialog_ok_btn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				exName = ((EditText)findViewById(R.id.add_dialog_name)).getText().toString();
				if (!exName.equals("")) {
					if (edit) {
						db.updateExerciseName(exName, exerciseId);
						Log.i(TAG, "Edit exercise: "+exName);
					} else {
						db.insertExercise(exName);
						Log.i(TAG, "Add New exercise: "+exName);
					}
				}
				finish();
			}
		});
	    // on cancel button click
		((Button)findViewById(R.id.add_dialog_cancel_btn)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});			
	}

	@Override
	public void onBackPressed() {
		finish();		
		super.onBackPressed();
	}
	
}
