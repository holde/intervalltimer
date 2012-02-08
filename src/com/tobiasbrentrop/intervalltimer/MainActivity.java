package com.tobiasbrentrop.intervalltimer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends BaseActivity {
	
	private static final String TAG = "IT: MainActivity";
	
	private Cursor cursor;
	private DbHelper db;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	ListView exerciseList = (ListView)findViewById(R.id.exerciseListLv);
    	db = ((IntervallTimerApp)getApplicationContext()).getDb();
        cursor = db.selectAllExercises();
        startManagingCursor(cursor);
        ListAdapter adapter = new SimpleCursorAdapter(
                this, // Context.
                R.layout.mainlistviewentry,
                cursor,
                new String[] {"name", "info"},
                new int[] {R.id.text1, R.id.text2});
        exerciseList.setAdapter(adapter);
        exerciseList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						Intent exDetail = new Intent(MainActivity.this, ExerciseDetailActivity.class);
						exDetail.putExtra("exerciseId", id);
						startActivity(exDetail);
						Log.i(TAG, "Show Detail for Exercise "+id);
			}
			
			
        	
		});
    }
}
