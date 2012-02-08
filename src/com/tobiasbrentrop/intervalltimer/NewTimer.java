package com.tobiasbrentrop.intervalltimer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class NewTimer extends BaseActivity implements OnClickListener {
	
	private static final String TAG = "IT: NewTimer";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newtimer);
        
        View OK = findViewById(R.id.newTimerCancel);
        OK.setOnClickListener(this);
        View cancel = findViewById(R.id.newTimerOK);
        cancel.setOnClickListener(this);
        
    }

	@Override
	public void onClick(View v) {
		Intent mainIntent = new Intent(this, MainActivity.class);
		switch(v.getId()) {
		case R.id.newTimerOK:
			addTimer();
			startActivity(mainIntent);
			break;
		case R.id.newTimerCancel:
			startActivity(mainIntent);
			break;
		}
	}


	private void addTimer() {
		try {
			int preps = Integer.parseInt((((EditText)findViewById(R.id.prepTime)).getText().toString()));
			int durs = Integer.parseInt((((EditText)findViewById(R.id.durationTime)).getText().toString()));
			int reps = Integer.parseInt((((EditText)findViewById(R.id.repetitionTime)).getText().toString()));
			int rests = Integer.parseInt((((EditText)findViewById(R.id.restTime)).getText().toString()));
			int[] times = {preps, durs, reps, rests};
			//addTimes(times);
		} catch (NumberFormatException e) {
			Log.e(TAG, "No Integer");
		}
	}
}