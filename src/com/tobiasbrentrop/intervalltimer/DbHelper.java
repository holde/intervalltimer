package com.tobiasbrentrop.intervalltimer;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class DbHelper {
	
   private static final String TAG = "IT: DbHelper";
 
   private static final String DATABASE_NAME = "intervalltimer.db";
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
 
   public void addNewExercise(String name) {
	   Cursor cursor = selectAllExercises();
	   // get next available position
	   insertExercise(name, cursor.getCount() + 1, "new exercise");
   }
   
   public long insertUnit(ExerciseUnit unit, long exerciseId) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", unit.getName());
	   cv.put("preptime", unit.getPreparationTime());
	   cv.put("durationtime", unit.getDuration());
	   cv.put("repetitions", unit.getRepetitions());
	   cv.put("resttime", unit.getRestTime());
	   // pos in db starts with 1
	   cv.put("position", selectUnitsFromExercise(exerciseId).getCount() + 1);
	   cv.put("exerciseid", exerciseId);
	   cv.put("info", unit.toString());
	   return db.insert(UNIT_TABLE, null, cv);
   }
   
   public long insertExercise(String name, int position, String info) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", name);
	   cv.put("position", position);
	   cv.put("info", info);
	   return this.db.insert(EXERCISE_TABLE, null, cv);
   }

   public void insertUnit(String name, int prep, int duration, int reps, int rest,
			long longExtra) {
		
	}   
 
   public void deleteAll() {
	   this.db.delete(UNIT_TABLE, null, null);
	   this.db.delete(EXERCISE_TABLE, null, null);
   }
   
   public void deleteExercise(long id) {
	   this.db.delete(EXERCISE_TABLE, "_id="+id, null);
	   this.db.delete(UNIT_TABLE, "exerciseid="+id, null);
   }
   public void deleteUnit(long id) {
	   this.db.delete(UNIT_TABLE, "_id="+id, null);		
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
   
   private static class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
 
      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + UNIT_TABLE +
        		    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        		    " name TEXT," +
        		    " preptime INTEGER," +
        		    " durationtime INTEGER," +
        		    " repetitions INTEGER," +
        		    " resttime INTEGER," +
        		    " position INTEGER," +
        		    " exerciseid INTEGER," +
        		    " info TEXT)");
         db.execSQL("CREATE TABLE " + EXERCISE_TABLE +
		    		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
		    		" name TEXT," +
		    		" position INTEGER, " +
		    		" info TEXT)");
         Log.i(TAG, "Create DB: (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + UNIT_TABLE);
         onCreate(db);
      }
   }

}