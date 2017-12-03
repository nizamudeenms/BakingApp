package com.example.nizam.bakingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nizam on 07-Nov-2017 007.
 */

public class BakingDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "baking.db";
    private static final int DATABASE_VERSION = 1;
    final String SQL_CREATE_BAKING_TABLE = "CREATE TABLE IF NOT EXISTS " + BakingContract.BakingEntry.BAKING_TABLE + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BakingContract.BakingEntry.COLUMN_BAKING_ID + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_NAME + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_SERVINGS + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_IMAGE + " BLOB,  " +
            " UNIQUE (" + BakingContract.BakingEntry.COLUMN_BAKING_ID + ") ON CONFLICT REPLACE);";

    final String SQL_CREATE_BAKING_ING_TABLE = "CREATE TABLE IF NOT EXISTS " + BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BakingContract.BakingEntry.COLUMN_BAKING_ID + " INTEGER NOT NULL, " +
            BakingContract.BakingEntry.COLUMN_BAKING_QUANTITY + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_MEASURE + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_INGREDIENT + " INTEGER NOT NULL, FOREIGN KEY(baking_id) REFERENCES baking_main_table(baking_id) );" ;

    final String SQL_CREATE_BAKING_STEPS_TABLE = "CREATE TABLE IF NOT EXISTS " + BakingContract.BakingEntry.BAKING_STEPS_TABLE + " (" +
            BakingContract.BakingEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            BakingContract.BakingEntry.COLUMN_BAKING_ID + " INTEGER NOT NULL, " +
            BakingContract.BakingEntry.COLUMN_BAKING_STEP_ID + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_SHORT_DESC + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_DESC + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_VIDEOURL + " INTEGER NOT NULL,  " +
            BakingContract.BakingEntry.COLUMN_BAKING_THUMBNAILURL + " INTEGER NOT NULL, FOREIGN KEY (baking_id) REFERENCES baking_main_table(baking_id)); " ;


    public BakingDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("inside Oncrate of sqllite ");

        db.execSQL(SQL_CREATE_BAKING_TABLE);
        Log.i("Table Created", "SQL_CREATE_BAKING_TABLE TableCreated");

        db.execSQL(SQL_CREATE_BAKING_STEPS_TABLE);
        Log.i("Table Created", "SQL_CREATE_BAKING_STEPS_TABLE TableCreated");

        db.execSQL(SQL_CREATE_BAKING_ING_TABLE);
        Log.i("Table Created", "SQL_CREATE_BAKING_ING_TABLE TableCreated");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.BAKING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.BAKING_STEPS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.BAKING_INGREDIENT_TABLE);
        onCreate(db);
        Log.i("DB Dropped", "DB Dropped");
    }
}
