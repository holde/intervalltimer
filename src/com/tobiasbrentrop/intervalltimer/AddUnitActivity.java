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
import com.tobiasbrentrop.IntervallPicker.TimePicker;
import com.tobiasbrentrop.intervalltimer.ExerciseUnit.Time;

public class AddUnitActivity extends Activity {
	
	private static final String TAG = "IT: AddUnitActivity";
	private DbHelper db;
	
	// ui
	private EditText et;
	private TimePicker mPrepPicker;
	private TimePicker mDurPicker;
	private TimePicker mRepPicker;
	private TimePicker mRestPicker;
	private TimePicker mCoolPicker;
	
	// state
	private String name;
	private int mPrep;
	private int mDur;
	private int mRep;
	private int mRest;
	private int mCool;
	private long exerciseId;
	private long unitId;
	private boolean edit;
	private ExerciseUnit exUnit;
	
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
		setContentView(R.layout.addunit);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		// ui elements
		et = (EditText)findViewById(R.id.add_dialog_name);
		mPrepPicker = (TimePicker)findViewById(R.id.add_dialog_prep);
		mDurPicker = (TimePicker)findViewById(R.id.add_dialog_dur);
		mRepPicker = (TimePicker)findViewById(R.id.add_dialog_rep);
		mRestPicker = (TimePicker)findViewById(R.id.add_dialog_rest);
		mCoolPicker = (TimePicker)findViewById(R.id.add_dialog_cool);
		
		// update title
		et.addTextChangedListener(UPDATE_TITLE);
		// get unit/exid
		exerciseId = getIntent().getLongExtra("exerciseId", 0);
		unitId = getIntent().getLongExtra("unitId", 0);
		// if new unit
		if (unitId == 0) {
			setTitle("New Exercise");
			edit = false;
		// if edit
		} else {
			edit = true;
			exUnit = 
			getValues();
			setTimePicker();
			setTitle(BaseActivity.timeString(db.getUnitTotalTime(unitId))+" "+db.getUnitName(unitId));
		}
        // on ok button click
		((Button)findViewById(R.id.add_dialog_ok_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// go back to list
				if (!name.equals("")) {
					exUnit = new ExerciseUnit(name, prep, duration, reps, rest);
					db.insertUnit(exUnit, exerciseId);
					Log.i(TAG, "Add New Unit: "+name);
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

	private void getValues() {
		mPrep = db.get
	}

	private void setTimePicker() {
		
		
	}
}
