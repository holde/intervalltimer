package com.tobiasbrentrop.intervalltimer;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
 
public class DbHelper {
	
   private static final String TAG = "IT: DbHelper";
 
   private static final String DATABASE_NAME = "intervalltimer.db";
   private static final int DATABASE_VERSION = 1;
   private static final String TABLE_NAME = "exerciseunits";
   private static final String TABLE_NAME2 = "exerciselist";
 
   private Context context;
   private SQLiteDatabase db;
 
   public DbHelper(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
   }
 
   public long insertUnit(String name, int preptime, int durationtime, int repetitions, int resttime, int position, int exerciseid) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", name);
	   cv.put("preptime", preptime);
	   cv.put("durationtime", durationtime);
	   cv.put("repetitions", repetitions);
	   cv.put("resttime", resttime);
	   cv.put("position", position);
	   cv.put("exerciseid", exerciseid);
	   return db.insert(TABLE_NAME, null, cv);
   }
   
   public long insertExercise(String name, int position, String info) {
	   ContentValues cv = new ContentValues();
	   cv.put("name", name);
	   cv.put("position", position);
	   cv.put("info", info);
	   return this.db.insert(TABLE_NAME2, null, cv);
   }
 
   public void deleteAll() {
      this.db.delete(TABLE_NAME, null, null);
      this.db.delete(TABLE_NAME2, null, null);
   }
 
   public Cursor selectAll() {
      Cursor cursor = this.db.query(TABLE_NAME, null, 
        null, null, null, null, "id asc");
      return cursor;
   }
   
   public Cursor selectUnitsFromExercise(long exerciseId) {
      Cursor cursor = this.db.query(TABLE_NAME,
    		  						null, 
    		  						"exerciseid="+Long.toString(exerciseId),
    		  						null, null, null,
    		  						"position asc");
  	  return cursor;	   
   }
   
   public Cursor selectAllExercises() {
      Cursor cursor = this.db.query(TABLE_NAME2,
    		  						new String[] {"_id", "name", "info"}, 
    		  						null, null, null, null,
    		  						"position asc");
      return cursor;
   }
   
   private static class OpenHelper extends SQLiteOpenHelper {
 
      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }
 
      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + TABLE_NAME +
        		    "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
        		    " name TEXT," +
        		    " preptime INTEGER," +
        		    " durationtime INTEGER," +
        		    " repetitions INTEGER," +
        		    " resttime INTEGER," +
        		    " position INTEGER," +
        		    " exerciseid)");
         db.execSQL("CREATE TABLE " + TABLE_NAME2 +
		    		"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
		    		" name TEXT," +
		    		" position INTEGER, " +
		    		" info TEXT)");
         Log.i(TAG, "Create DB: (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)");
      }
 
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
      }
   }
}