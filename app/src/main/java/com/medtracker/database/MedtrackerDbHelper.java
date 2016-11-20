package com.medtracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Debug;

import static com.medtracker.database.MedTrackerContract.*;

/**
 * Created by home on 18/11/2016.
 */

public class MedtrackerDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MedTracker.db";


    public MedtrackerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        createTables(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean createUser (String email, String firstName, String lastName, int pin, int googleAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.COLUMN_NAME_EMAIL, email);
        contentValues.put(User.COLUMN_NAME_FIRST_NAME, firstName);
        contentValues.put(User.COLUMN_NAME_LAST_NAME, lastName);
        contentValues.put(User.COLUMN_NAME_PIN, pin);
        contentValues.put(User.COLUMN_NAME_GOOGLE_ACCOUNT, googleAccount);
        db.insert(User.TABLE_NAME, null, contentValues);
        return true;
    }

    private void createTables(SQLiteDatabase db) {
        db.execSQL(User.SQL_CREATE_TABLE);
        db.execSQL(Medication.SQL_CREATE_TABLE);
        db.execSQL(Prescription.SQL_CREATE_TABLE);
        db.execSQL(Record.SQL_CREATE_TABLE);
        db.execSQL(Alarm.SQL_CREATE_TABLE);
        db.execSQL(AlarmManager.SQL_CREATE_TABLE);
        db.execSQL(Glossary.SQL_CREATE_TABLE);
        db.execSQL(Pharmacies.SQL_CREATE_TABLE);
        System.out.println("Created Database sucesfully!");
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
