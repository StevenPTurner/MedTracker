package com.medtracker.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by home on 18/11/2016.
 */

public final class MedTrackerContract {

    private MedTrackerContract() {}

    public static class User implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_PIN = "pin";
        public static final String COLUMN_NAME_GOOGLE_ACCOUNT = "google_account";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_EMAIL + " TEXT PRIMARY KEY," +
                COLUMN_NAME_FIRST_NAME + " TEXT," +
                COLUMN_NAME_LAST_NAME + " TEXT," +
                COLUMN_NAME_PIN + " INT" +
                COLUMN_NAME_GOOGLE_ACCOUNT + " BOOLEAN )";

    }

    public static class Medication implements BaseColumns {}
    public static class Prescription implements BaseColumns {}
    public static class Record implements BaseColumns {}
    public static class Alarm implements BaseColumns {}
    public static class AlarmManager implements BaseColumns {}
    public static class Glossary implements BaseColumns {}
    public static class Pharmacies implements BaseColumns {}
}
