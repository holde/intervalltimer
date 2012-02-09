package com.tobiasbrentrop.intervalltimer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends BaseActivity {
	
	private static final String TAG = "IT: MainActivity";
	
	private DbHelper db;
	Cursor cursor;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // on button click
		((Button)findViewById(R.id.mainAddTimerBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addTimer = new Intent(MainActivity.this, AddExerciseActivity.class);
				startActivityForResult(addTimer, 0);
			}
		});       
        // listview stuff
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
        registerForContextMenu(exerciseList);
//        exerciseList.setOnItemLongClickListener(new OnItemLongClickListener() {
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				Log.i(TAG, "pos: "+position+" id: "+id);
//				return true;
//			}
//		});
        exerciseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						Intent exDetail = new Intent(MainActivity.this, ExerciseDetailActivity.class);
						exDetail.putExtra("exerciseId", id);
						startActivity(exDetail);
						Log.i(TAG, "Show detail for exercise "+id);
			}
		});
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, 0, 0, "Edit");
		menu.add(0, 1, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case 0: Log.i(TAG, "edit");
				break;
		case 1: db.deleteExercise(info.id);
				// TODO: improve, requery deprecated
				cursor.requery();
				Log.i(TAG, "delete");
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: improve, requery deprecated		
		cursor.requery();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
