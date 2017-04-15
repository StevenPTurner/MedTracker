package com.medtracker.Utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.medtracker.Models.Record;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Used for sending e-mails with attachments
 */

public class EmailHelper {
    private static final String TAG = LogTag.emailHelper;
    private final String fileName = "records.csv";

    //acts as accessor to class and preforms actions to return an intent for sending the email
    public Intent emailRecords(ArrayList<Record> records) {
        String csvFile = getCSVFromRecords(records);
        writeFile(csvFile);

        File file = new File(Environment.getExternalStorageDirectory(), "/" + fileName);
        Uri path = Uri.fromFile(file);
        Log.d(TAG, "File path: " + path.toString());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Record of Doses Taken");
        intent.putExtra(Intent.EXTRA_TEXT, "Please find a csv attached.");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        return intent;
    }

    //formats the records into a string representing a CSV file
    private String getCSVFromRecords(ArrayList<Record> records) {
        String columnTitles = "Medication Name,Dose,Time,Date \n";
        String csvFile = columnTitles;

        for(int i=0; i<records.size(); i++) {
            Record record = records.get(i);
            String name = Convert.keyToName(record.getMedication_key());
            int dose = record.getDose();

            String time = record.getHour() + ":" +
                    record.getMinute() + ":" +
                    record.getSecond();

            String date = record.getDay() + "/" +
                    record.getMonth() + "/" +
                    record.getYear();

            csvFile = csvFile + name + "," + dose + "," + time + "," + date + "\n";
        }
        Log.d(TAG, "csv File created");
        return csvFile;
    }

    //writes file to external storage on device to be read by chosen app
    private void writeFile(String content) {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "records.csv");
            Log.d(TAG, Environment.getExternalStorageDirectory().toString() + "/records.csv");
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }
}
