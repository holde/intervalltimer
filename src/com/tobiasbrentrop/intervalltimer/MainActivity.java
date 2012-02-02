package com.tobiasbrentrop.intervalltimer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends BaseActivity implements OnClickListener {
	
	private static final String TAG = "IT: MainActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View addTimer = findViewById(R.id.mainAddTimer);
        addTimer.setOnClickListener(this);
        View go = findViewById(R.id.mainGo);
        go.setOnClickListener(this);
    }

    @Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.mainAddTimer :
			((TextView)findViewById(R.id.mainAddTimer)).setBackgroundColor(Color.CYAN);
			Intent newTimerIntent = new Intent(this, NewTimer.class);
			startActivity(newTimerIntent);
			Log.d(TAG, "starting newTimer");
			break;
		case R.id.mainGo :
			Intent runTimerIntent = new Intent(this, RunTimer.class);
			startActivity(runTimerIntent);
			break;
		}
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "Resume");
	}
    
    public void fillTimer(int[] times) {
		TextView timerView = new TextView(this);
		timerView.setText("der hier hat sekunden");
		LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		timerView.setLayoutParams(p);
		((LinearLayout)findViewById(R.id.mainLL)).addView(timerView);
    }

}
