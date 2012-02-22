package com.tobiasbrentrop.intervalltimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends BaseActivity {
	
	private static final String TAG = "IT: MainActivity";
	
	private DbHelper db;
	Cursor cursor;
	private long exId;
	
	final static int EDIT = 0;
	final static int DELETE = 1;
	final static int DETAILS = 2;
	final static int START = 3;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // on button click
		((ImageButton)findViewById(R.id.bottom_add_btn)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addTimer = new Intent(MainActivity.this, AddExerciseActivity.class);
				startActivityForResult(addTimer, 0);
			}
		});       
        // listview stuff
    	ListView exerciseList = (ListView)findViewById(R.id.listview);
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
        exerciseList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
						Intent exDetail = new Intent(MainActivity.this, ExerciseDetailActivity.class);
						exDetail.putExtra("exerciseId", id);
						startActivity(exDetail);
						// fade transition
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
						Log.i(TAG, "Show detail for exercise "+id);
			}
		});
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DETAILS, 0, "Details");
		menu.add(0, START, 0, "Start");
		menu.add(0, EDIT, 0, "Edit");
		menu.add(0, DELETE, 0, "Delete");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		exId = info.id;
		switch (item.getItemId()) {
		case EDIT:	Intent addEx = new Intent(MainActivity.this, AddExerciseActivity.class);
				addEx.putExtra("exerciseId", exId);
				startActivityForResult(addEx, 0);
				break;
		case DELETE: showDialog(0);
				Log.i(TAG, "delete");
				break;
		case DETAILS: Intent exDetail = new Intent(MainActivity.this, ExerciseDetailActivity.class);
				exDetail.putExtra("exerciseId", exId);
				startActivity(exDetail);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
		case START: Intent runTimer = new Intent(MainActivity.this, RunTimer.class);
				runTimer.putExtra("exerciseId", exId);
				startActivity(runTimer);
				overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO: improve, requery deprecated		
		cursor.requery();

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(MainActivity.this)
        .setIcon(R.drawable.alert_dialog_icon)
        .setTitle(R.string.alert_dialog_delete)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            	db.deleteExercise(exId);
				// TODO: improve, requery deprecated
        		cursor.requery();
            }
        })
        .setNegativeButton(R.string.cancel, null)
        .create();
	}
}
