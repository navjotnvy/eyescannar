package com.navjot.faceproject.alarmforhealth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by T00523221 on 3/24/2017.
 */

public class DBAdapter1 {

    /////////////////////////////////////////////////////////////////////
    //	Constants & Data
    /////////////////////////////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter1";
    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;

    // [TO_DO_A2]
    // TODO: Change the field names (column names) of your table
    public static final String KEY_IMAGE = "image";
    // [TO_DO_A3]
    // Update the field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_IMAGE = 1;
    // [TO_DO_A4]
    // Update the ALL-KEYS string array
    public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_IMAGE};

    // [TO_DO_A5]
    // DB info: db name and table name.
    public static final String DATABASE_NAME = "MyDb1";
    public static final String DATABASE_TABLE = "mainTable1";

    // [TO_DO_A6]
    // Track DB version
    public static final int DATABASE_VERSION = 1;


    // [TO_DO_A7]
    // DATABASE_CREATE SQL command
    private static final String DATABASE_CREATE_SQL1 =
            "create table " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " integer primary key autoincrement, "
                    + KEY_IMAGE        + " image text not null "
                    + ");";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper1;
    private SQLiteDatabase db1;

    // ==================
    //	Public methods:
    // ==================

    public DBAdapter1(Context ctx) {
        this.context = ctx;
        myDBHelper1 = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter1 open1() {
        db1 = myDBHelper1.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close1() {
        myDBHelper1.close();
    }



    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow1(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db1.delete(DATABASE_TABLE, where, null) != 0;
    }

    // Delete all records
    public void deleteAll1() {
        Cursor c = getAllRows1();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow1(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all rows in the database.
    public Cursor getAllRows1() {
        String where = null;
        Cursor c = db1.query(true, DATABASE_TABLE, ALL_KEYS,
                where , null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow1(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = 	db1.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /*
    // Change an existing row to be equal to new data. for image
    public boolean updateRow1(long rowId, byte[] image) {
        String where = KEY_ROWID + "=" + rowId;
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_IMAGE, image);
        // Insert it into the database.
        return db1.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    // Add a new set of values to the database. for image
    public long insertRow1(byte[] image) {
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IMAGE, image);
        // Insert it into the database.
        return db1.insert(DATABASE_TABLE, null, initialValues);
    }
*/


    public long insertRow1(String img) {
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_IMAGE, img);
        // Insert it into the database.
        return db1.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean updateRow1(long rowId, String img) {
        String where = KEY_ROWID + "=" + rowId;
        // [TO_DO_A8]
        // Update data in the row with new fields.
        // Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_IMAGE, img);
        // Insert it into the database.
        return db1.update(DATABASE_TABLE, newValues, where, null) != 0;
    }


    // ==================
    //	Private Helper Classes:
    // ==================

    /**
     * Private class which handles database creation and upgrading.
     * Used to handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // Recreate new database:
            onCreate(_db);
        }
    }
}
