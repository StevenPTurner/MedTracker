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
                COLUMN_NAME_PIN + " INTEGER," +
                COLUMN_NAME_GOOGLE_ACCOUNT + " INTEGER )";
    }

    public static class Medication implements BaseColumns {
        public static final String TABLE_NAME = "medication";
        public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_NAME_INSTRUCTUIONS = "instructions";
        public static final String COLUMN_NAME_DOSAGE = "dosage";
        public static final String COLUMN_NAME_HAS_PRESCRIPTION = "has_prescription";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MEDICATION_NAME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_INSTRUCTUIONS + " TEXT," +
                        COLUMN_NAME_DOSAGE + " INTEGER," +
                        COLUMN_NAME_HAS_PRESCRIPTION + " INTEGER )";
    }

    public static class Prescription implements BaseColumns {
        public static final String TABLE_NAME = "prescription";
        public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_NAME_DOSE_PER_PRESCRIPTION = "dose_per_prescription";
        public static final String COLUMN_NAME_DOSE_LEFT = "dose_left";
        public static final String COLUMN_NAME_EXPECTED_DURATION = "expected_duration";
        public static final String COLUMN_NAME_DOSE_PER_DAY = "dose_per_day";
        public static final String COLUMN_NAME_NEXT_ALARM = "next_alarm";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MEDICATION_NAME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_DOSE_PER_PRESCRIPTION + " INTEGER," +
                        COLUMN_NAME_DOSE_LEFT + " INTEGER," +
                        COLUMN_NAME_EXPECTED_DURATION + " INTEGER," +
                        COLUMN_NAME_DOSE_PER_DAY + " INTEGER," +
                        COLUMN_NAME_NEXT_ALARM + " TEXT )";
    }

    public static class Record implements BaseColumns {
        public static final String TABLE_NAME = "record";
        public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";
        public static final String COLUMN_NAME_DOSE = "dose";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_DATE_TIME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_MEDICATION_NAME + " TEXT," +
                        COLUMN_NAME_DOSE + " INTEGER )";
    }

    public static class Alarm implements BaseColumns {
        public static final String TABLE_NAME = "alarm";
        public static final String COLUMN_NAME_ALARM_ID = "alarm_id";
        public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_NAME_SLEEP_TIMER = "sleep_timer";
        public static final String COLUMN_NAME_SLEEP_BEFORE_SKIP = "sleep_before_skip;";
        public static final String COLUMN_NAME_DATE_TIME = "date_time";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_ALARM_ID + " INTEGER," +
                        COLUMN_NAME_MEDICATION_NAME + " TEXT," +
                        COLUMN_NAME_SLEEP_TIMER + " INTEGER," +
                        COLUMN_NAME_SLEEP_BEFORE_SKIP + " INTEGER," +
                        COLUMN_NAME_DATE_TIME + " TEXT," +
                        " PRIMARY KEY (" + COLUMN_NAME_ALARM_ID + "," +  COLUMN_NAME_MEDICATION_NAME + "))";
    }

    public static class AlarmManager implements BaseColumns {
        public static final String TABLE_NAME = "alarm_manager";
        public static final String COLUMN_NAME_MEDICATION_NAME = "medication_name";
        public static final String COLUMN_NAME_CURRENT_ALARM = "current_alarm";
        public static final String COLUMN_NAME_TOTAL_ALARM = "total_alarm";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_MEDICATION_NAME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_CURRENT_ALARM + " TEXT," +
                        COLUMN_NAME_TOTAL_ALARM + " INTEGER, )";
    }

    public static class Glossary implements BaseColumns {
        public static final String TABLE_NAME = "Glossary";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NAME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_DESCRIPTION + " TEXT )";
    }

    public static class Pharmacies implements BaseColumns {
        public static final String TABLE_NAME = "Pharmacies";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LOCATION = "location";
        public static final String COLUMN_NAME_ADDRESS = "address";

        public static final String SQL_CREATE_TABLE_USER =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        COLUMN_NAME_NAME + " TEXT PRIMARY KEY," +
                        COLUMN_NAME_LOCATION + " TEXT," +
                        COLUMN_NAME_ADDRESS + " TEXT )";
    }
}
