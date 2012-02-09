package com.tobiasbrentrop.intervalltimer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class ExerciseDetailActivity extends BaseActivity {

	private static final String TAG = "IT: ExerciseDetailActivity";
	private Exercise currentExercise;
	private DbHelper db;
	Cursor unitCursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisedetail);
		final long exerciseId = getIntent().getLongExtra("exerciseId", 0);
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		Log.i(TAG, "exid: "+exerciseId);
		unitCursor = db.selectUnitsFromExercise(exerciseId);
		currentExercise = new Exercise(unitCursor);
		setTitle(currentExercise.getExerciseTitle()+": "+currentExercise.toString());
		
		// listview stuff
		unitCursor = db.selectUnitsFromExercise(exerciseId);
        startManagingCursor(unitCursor);
        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.exdetaillistviewentry,
                unitCursor,
                new String[] {"name", "info"},
                new int[] {R.id.text1, R.id.text2});
        ListView exDetailLv = (ListView)findViewById(R.id.exerciseDetailsLv);
        exDetailLv.setAdapter(adapter);
        registerForContextMenu(exDetailLv);
        exDetailLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						Intent exDetail = new Intent(ExerciseDetailActivity.this, ExerciseDetailActivity.class);
						exDetail.putExtra("exerciseId", id);
						Log.i(TAG, "Show Detail for Exercise "+id);
			}
		});
        
        // on button click
		((Button)findViewById(R.id.exerciseRunBt)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent runTimer = new Intent(ExerciseDetailActivity.this, RunTimer.class);
				runTimer.putExtra("exerciseId", exerciseId);
				startActivity(runTimer);
			}
		});
		((Button)findViewById(R.id.exerciseAddBtn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addUnit = new Intent(ExerciseDetailActivity.this, AddUnitActivity.class);
				addUnit.putExtra("exerciseId", exerciseId);
				startActivityForResult(addUnit, 0);
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
		case 1: db.deleteUnit(info.id);
				// TODO: improve, requery deprecated
				unitCursor.requery();
				Log.i(TAG, "delete");
				break;
		}
		return super.onContextItemSelected(item);
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: improve, requery deprecated		
		unitCursor.requery();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
	
