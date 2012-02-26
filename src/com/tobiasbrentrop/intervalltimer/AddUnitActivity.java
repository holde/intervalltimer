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
import com.tobiasbrentrop.IntervallPicker.TimePicker.OnTimeChangedListener;
import com.tobiasbrentrop.intervalltimer.ExerciseUnit.Time;

public class AddUnitActivity extends Activity {
	
	private static final String TAG = "IT: AddUnitActivity";
	private DbHelper db;
	
	// ui
	private EditText et;
	private TimePicker mPrepPicker;
	private TimePicker mWorkPicker;
	private TimePicker mRepPicker;
	private TimePicker mRestPicker;
	private TimePicker mCoolPicker;
	
	// state
	private String[] title = new String[2];
	private String mName;
	private int mPrep;
	private int mWork;
	private int mRep;
	private int mRest;
	private int mCool;
	private int mTotal;
	private long exerciseId;
	private long unitId;
	private boolean edit;
	private ExerciseUnit exUnit;
	
	/**
	 * TextWatcher for unit name edittext
	 */
	private final TextWatcher UPDATE_TITLE = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			title[1] = et.getText().toString();
			mName = title[1];
			updateTitle();			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	/**
	 * Listener for timepicker. changes title
	 */
	private final OnTimeChangedListener PICKER_LISTENER = new OnTimeChangedListener() {
		 public void onTimeChanged(TimePicker view, int minute, int second) {
			 int temp = minute*60 + second;
			 switch (view.getId()) {
			 case R.id.add_dialog_prep:
				 mPrep = temp; 
				 break;
			 case R.id.add_dialog_dur:
				 mWork = temp;
				 break;
			 case R.id.add_dialog_rep:
				 mRep = second;
				 break;
			 case R.id.add_dialog_rest:
				 mRest = temp;
				 break;
			 case R.id.add_dialog_cool:
				 mCool = temp;
				 break;
			 }
			 calculateTimes();
			 updateTitle();
		 }
	 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addunit);
		// global db
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		// ui elements
		et = (EditText)findViewById(R.id.add_dialog_name);
		mPrepPicker = (TimePicker)findViewById(R.id.add_dialog_prep);
		mWorkPicker = (TimePicker)findViewById(R.id.add_dialog_dur);
		mRepPicker = (TimePicker)findViewById(R.id.add_dialog_rep);
		mRestPicker = (TimePicker)findViewById(R.id.add_dialog_rest);
		mCoolPicker = (TimePicker)findViewById(R.id.add_dialog_cool);
		// set listener
		mPrepPicker.setOnTimeChangedListener(PICKER_LISTENER);
		mWorkPicker.setOnTimeChangedListener(PICKER_LISTENER);
		mRepPicker.setOnTimeChangedListener(PICKER_LISTENER);
		mRestPicker.setOnTimeChangedListener(PICKER_LISTENER);
		mCoolPicker.setOnTimeChangedListener(PICKER_LISTENER);
		et.addTextChangedListener(UPDATE_TITLE);
		// get unit/exid
		exerciseId = getIntent().getLongExtra("exerciseId", 0);
		unitId = getIntent().getLongExtra("unitId", 0);
		// if new unit
		if (unitId == 0) {
			title[1] = "New Exercise"; 
			updateTitle();
			edit = false;
		// if edit
		} else {
			edit = true;
			exUnit = db.getUnit(unitId);
			getValues(exUnit);
			setTimePicker();
			title[1] = mName;
			et.setText(mName);
			updateTitle();
		}
        // on ok button click
		((Button)findViewById(R.id.add_dialog_ok_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exUnit = new ExerciseUnit(mName, mPrep, mWork, mRep, mRest, mCool);
				if (!edit) {
					db.insertUnit(exUnit, exerciseId);
					Log.i(TAG, "Add New Unit: "+mName);
				} else {
					db.updateUnit(unitId, exerciseId, exUnit);
					Log.i(TAG, "Edited Unit: "+mName);
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

	/**
	 * Set local time parameters
	 * @param unit Current unit
	 */
	private void getValues(ExerciseUnit unit) {
		mPrep = unit.getTime(Time.PREPARATION);
		mWork = unit.getTime(Time.WORKOUT);
		mRep = unit.getTime(Time.REPETITIONS);
		mRest = unit.getTime(Time.REST);
		mCool = unit.getTime(Time.COOL_DOWN);
		mTotal = unit.getTime(Time.TOTAL);
		mName = unit.getName();
	}

	/**
	 * Sets the Picker to the correct times
	 */
	private void setTimePicker() {
		mPrepPicker.setTime(mPrep);
		mWorkPicker.setTime(mWork);
		mRepPicker.setTime(mRep);
		mRestPicker.setTime(mRest);
		mCoolPicker.setTime(mCool);
	}
	
	/**
	 * Updates title of dialog
	 */
	private void updateTitle() {
		title[0] = BaseActivity.timeString(mTotal, true);
		setTitle(title[0]+" "+title[1]);
	}

	/**
	 * Recalculate total time
	 */
	private void calculateTimes() {
		if (mRep != 0) {
			mTotal = mPrep + (mWork + mRest) * (mRep - 1) + mWork + mCool; 
		} else {
			mTotal = mPrep + mCool;
		}
	}
}
	 
