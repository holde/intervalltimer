package com.tobiasbrentrop.intervalltimer;
 
import com.tobiasbrentrop.intervalltimer.ExerciseUnit.Time;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DbHelper {
	
   private static final String TAG = "IT: DbHelper";
 
   private static final String DATABASE_NAME = "itimer.db";
   private static final int DATABASE_VERSION = 1;
   private static final String UNIT_TABLE = "exerciseunits";
   private static final String EXERCISE_TABLE = "exerciselist";
 
   private Context context;
   private SQLiteDatabase db;
 
   public DbHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();      
   }
   /** GETTER */ 
   public String getExerciseName(long id) {
	  return getStringColumn(id, EXERCISE_TABLE, "name");
   }
   public String getExerciseInfo(long id) {
	   return getStringColumn(id, EXERCISE_TABLE, "info");
   }
   public int getExerciseUnitCount(long id) {
	   return getIntColumn(id, EXERCISE_TABLE, "unitcount");
   }
   public String getUnitName(long id) {
	   return getStringColumn(id, UNIT_TABLE, "name");
   }
   public String getStringColumn(long id, String table, String column) {
	   Cursor cursor = this.db.query(table,
				new String[] {column}, 
				"_id="+id,
				null, null, null, null);
	  if (cursor.moveToFirst()) {
		  return cursor.getString(0);
	  } else {
		  return "error";
	  }
   }

   public int getIntColumn(long id, String table, String column) {
	   Cursor cursor = this.db.query(table,
				new String[] {column}, 
				"_id="+id,
				null, null, null, null);
	  if (cursor.moveToFirst()) {
		  return cursor.getInt(0);
	  } else {
		  return Integer.MAX_VALUE;
	  }
   }
   public int getUnitTotalTime(long id) {
	   return getIntColumn(id, UNIT_TABLE, "totaltime");
   }
   public int getUnitPosition(long id) {
	   Cursor cursor = this.db.query(UNIT_TABLE,
				new String[] {"position"}, 
				"_id="+id,
				null, null, null,
				"position asc");
	  if (cursor.moveToFirst()) {
		  return cursor.getInt(0);
	  } else {
		  return Integer.MAX_VALUE;
	  }
   }

   public ExerciseUnit getUnit(long id) {
	   Cursor cursor = selectUnitFromId(id);
	   if (cursor.moveToFirst()) {
		   return new ExerciseUnit(
					cursor.getString(1), 	// name
					cursor.getInt(2),		// prep
					cursor.getInt(3),		// workout
					cursor.getInt(4),		// rep
					cursor.getInt(5),		// rest
					cursor.getInt(6));		// cooldown
	   } else {
		   return null;
	   }
   }

   public Cursor selectAllUnits() {
      Cursor cursor = this.db.query(UNIT_TABLE, null, 
    		  	null, null, null, null, "id asc");
      return cursor;
	   }
   public Cursor selectUnitsFromExercise(long exerciseId) {
      Cursor cursor = this.db.query(UNIT_TABLE,
				null, 
				"exerciseid="+Long.toString(exerciseId),
				null, null, null,
				"position asc");
  	  return cursor;	   
   }
   public Cursor selectAllExercises() {
      Cursor cursor = this.db.query(EXERCISE_TABLE,
				null, null, null, null, null,
				"position desc");
      return cursor;
   }
   public Cursor selectUnitFromId(long unitId) {
      Cursor cursor = this.db.query(UNIT_TABLE,
				null, 
				"_id="+Long.toString(unitId),
				null, null, null, null);
      return cursor;	   
   }   
   /** ADDER */
   public long insertExercise(String name) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", name);
	   // get next available position
	   Cursor cursor = selectAllExercises();
	   int position;
	   if (cursor == null) {
		   position = 1;
	   } else {
		   position = cursor.getCount();
	   }
	   cv.put("position", position);
	   // make toString
	   cv.put("info", "0 units ("+BaseActivity.timeString(0, true)+")");
	   return this.db.insert(EXERCISE_TABLE, null, cv);
   }
   public long insertUnit(ExerciseUnit unit, long exerciseId) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", unit.getName());
	   cv.put("preptime", unit.getTime(Time.PREPARATION));
	   cv.put("workouttime", unit.getTime(Time.WORKOUT));
	   cv.put("repetitions", unit.getTime(Time.REPETITIONS));
	   cv.put("resttime", unit.getTime(Time.REST));
	   cv.put("cooltime", unit.getTime(Time.COOL_DOWN));
	   // pos in db starts with 1
	   cv.put("position", selectUnitsFromExercise(exerciseId).getCount() + 1);
	   cv.put("exerciseid", exerciseId);
	   cv.put("info", unit.getInfo());
	   // total duration of this unit
	   cv.put("totaltime", unit.getTime(Time.TOTAL));
	   // update total time of exercise
	   updateExerciseTime(exerciseId, unit.getTime(Time.TOTAL));
	   updateExerciseUnitCount(exerciseId, 1);
	   updateExerciseInfo(exerciseId);
	   return db.insert(UNIT_TABLE, null, cv);
   }
   /** UPDATER */
   public int updateExerciseName(String name, long exerciseId) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", name);
	   return db.update(EXERCISE_TABLE, cv, "_id="+exerciseId, null);
   }
   private void updateExerciseTime(long exerciseId, int time) {
	   this.db.execSQL("UPDATE "+EXERCISE_TABLE+" SET time=time+"+time+" WHERE _id="+exerciseId);
   }
   private void updateExerciseUnitCount(long exerciseId, int amount) {
	   this.db.execSQL("UPDATE "+EXERCISE_TABLE+" SET unitcount=unitcount+"+amount+" WHERE _id="+exerciseId);
   }
   private void updateExerciseInfo(long exerciseId) {
	   String info = ""+getIntColumn(exerciseId, EXERCISE_TABLE, "unitcount")+
	   				 " units ("+BaseActivity.timeString(getIntColumn(exerciseId, EXERCISE_TABLE, "time"), true)+")";
	   this.db.execSQL("UPDATE "+EXERCISE_TABLE+" SET info=\""+info+"\" WHERE _id="+exerciseId);
   }
   public void updateUnit(long unitId, long exerciseId, ExerciseUnit unit) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", unit.getName());
	   cv.put("preptime", unit.getTime(Time.PREPARATION));
	   cv.put("workouttime", unit.getTime(Time.WORKOUT));
	   cv.put("repetitions", unit.getTime(Time.REPETITIONS));
	   cv.put("resttime", unit.getTime(Time.REST));
	   cv.put("cooltime", Time.COOL_DOWN);
	   cv.put("info", unit.getInfo());
	   // total duration of this unit
	   cv.put("totaltime", unit.getTime(Time.TOTAL));
	   // update total time of exercise
	   updateExerciseTime(exerciseId, unit.getTime(Time.TOTAL) - getUnitTotalTime(unitId));
	   updateExerciseInfo(exerciseId);
	   db.update(UNIT_TABLE, cv, "_id="+unitId, null);
   }
   /** DELETER */
   public void deleteAll() {
	   this.db.delete(UNIT_TABLE, null, null);
	   this.db.delete(EXERCISE_TABLE, null, null);
   }
   public void deleteExercise(long id) {
	   int position = getIntColumn(id, EXERCISE_TABLE, "position");
	   // delete exercise and respectiv units
	   this.db.delete(EXERCISE_TABLE, "_id="+id, null);
	   this.db.delete(UNIT_TABLE, "exerciseid="+id, null);
	   // update exercise positions
	   this.db.execSQL("UPDATE "+EXERCISE_TABLE+" SET position=position-1 WHERE position > "+position);
   }
   public void deleteUnit(long unitId, long exerciseId) {
	   int position = getIntColumn(unitId, UNIT_TABLE, "position");
	   updateExerciseTime(exerciseId, (-1)*getIntColumn(unitId, UNIT_TABLE, "totaltime"));
	   Log.d(TAG, ""+getIntColumn(unitId, UNIT_TABLE, "totaltime"));
	   updateExerciseUnitCount(exerciseId, -1);
	   updateExerciseInfo(exerciseId);
	   
	   this.db.delete(UNIT_TABLE, "_id="+unitId, null);
	   // update unit positions
	   this.db.execSQL("UPDATE "+UNIT_TABLE+" SET position=position-1 WHERE position > "+position+" AND exerciseid = "+exerciseId);
	}
   
   private class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
 
      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + UNIT_TABLE +
        		    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        		    " name TEXT," +
        		    " preptime INTEGER," +
        		    " workouttime INTEGER," +
        		    " repetitions INTEGER," +
        		    " resttime INTEGER," +
        		    " cooltime INTEGER," +
        		    " position INTEGER," +
        		    " exerciseid INTEGER," +
        		    " info TEXT," +
        		    " totaltime INTEGER)");
         db.execSQL("CREATE TABLE " + EXERCISE_TABLE +
		    		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
		    		" name TEXT," +
		    		" position INTEGER," +
		    		" info TEXT," +
		    		" unitcount INTEGER DEFAULT 0," +
		    		" time INTEGER DEFAULT 0)");
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + UNIT_TABLE);
         onCreate(db);
      }
   }

}