package com.tobiasbrentrop.intervalltimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ExerciseDetailActivity extends BaseActivity {

	private static final String TAG = "IT: ExerciseDetailActivity";
	private DbHelper db;
	Cursor unitCursor;
	private long unitId;
	private long exerciseId;

	final static int EDIT = 0;
	final static int DELETE = 1;
	final static int DETAILS = 2;
	final static int START = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exercisedetail);
		// get current id from extra
		exerciseId = getIntent().getLongExtra("exerciseId", 0);
		// global db
		db = ((IntervallTimerApp)getApplicationContext()).getDb();
		// set title
		((TextView)findViewById(R.id.topbar_title)).setText(db.getExerciseName(exerciseId)+"\n"+db.getExerciseInfo(exerciseId));
		// listview stuff
		unitCursor = db.selectUnitsFromExercise(exerciseId);
        startManagingCursor(unitCursor);
        ListAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.exdetaillistviewentry,
                unitCursor,
                new String[] {"name", "info"},
                new int[] {R.id.text1, R.id.text2});
        ListView exDetailLv = (ListView)findViewById(R.id.listview);
        exDetailLv.setAdapter(adapter);
        registerForContextMenu(exDetailLv);
        exDetailLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						startEditActivity(exerciseId, id);
			}
		});
        // on button click
		((ImageButton)findViewById(R.id.bottom_run_btn)).setOnClickListener(new OnClickListener() {
			// run timer
			@Override
			public void onClick(View v) {
				Intent runTimer = new Intent(ExerciseDetailActivity.this, RunTimer.class);
				runTimer.putExtra("exerciseId", exerciseId);
				startActivity(runTimer);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			}
		});
		((ImageButton)findViewById(R.id.bottom_add_btn)).setOnClickListener(new OnClickListener() {
			// add/edit unit
			@Override
			public void onClick(View v) {
				startEditActivity(exerciseId, 0);
			}
		});		
	}
	protected void startEditActivity(long exerciseId, long unitId) {
		Intent addUnit = new Intent(ExerciseDetailActivity.this, AddUnitActivity.class);
		addUnit.putExtra("exerciseId", exerciseId);
		addUnit.putExtra("unitId", unitId);
		Log.i(TAG, "Edit Unit "+unitId+" from Ex "+exerciseId);
		startActivityForResult(addUnit, 0);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
//		menu.add(0, DETAILS, 0, "Details");
//		menu.add(0, START, 0, "Start");
		menu.add(0, EDIT, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		unitId = info.id;
		switch (item.getItemId()) {
		// edit unit
		case EDIT: startEditActivity(exerciseId, unitId);
				break;
		// delete unit
		case DELETE: showDialog(0);
				break;
		}
		return super.onContextItemSelected(item);
	}	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO: improve, requery deprecated		
		unitCursor.requery();
		// TODO: maybe not efficient enough
		// set title
		((TextView)findViewById(R.id.topbar_title)).setText(db.getExerciseName(exerciseId)+"\n"+db.getExerciseInfo(exerciseId));
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	// confirmation dialog
	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(ExerciseDetailActivity.this)
        .setIcon(R.drawable.alert_dialog_icon)
        .setTitle(R.string.alert_dialog_delete)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	db.deleteUnit(unitId, exerciseId);
				// TODO: improve, requery deprecated
        		unitCursor.requery();
            }
        })
        .setNegativeButton(R.string.cancel, null)
        .create();
	}	
}
	
