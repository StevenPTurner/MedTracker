package com.medtracker.Utilities;

/**
 * used to hold tags in one place for ease of access
 */

public final class LogTag {
    //Activities
    public static final String createAccountActivity = "LogCreateAccActivity";
    public static final String homeActivity = "LogHomeActivity";
    public static final String signInActivity = "LogSignInActivity";
    public static final String takeDoseActivity = "LogTakeDoseActivity";
    public static final String passwordResetActivity = "LogPassResetActivity";

    //Fragments
    public static final String medicationListFragment = "LogMedListFragment";
    public static final String recordListFragment = "LogRecordListFragment";
    public static final String alarmListFragment = "LogAlarmListFragment";
    public static final String alarmMedicationFragment = "LogAlarmMedFragment";
    public static final String pharmacyLogFragment = "LogPharmacyMapFragment";
    public static final String statisticsFragment = "LogStatisticsFragment";

    //Adapters
    public static final String alarmAdapter = "LogAlarmAdapter";

    //Misc
    public static final String notificationManager = "LogNotificationManager";
    public static final String emailHelper = "LogEmailHelper";
    public static final String factory = "LogFactory";
}
