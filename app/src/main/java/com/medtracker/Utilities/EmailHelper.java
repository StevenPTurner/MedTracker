package com.medtracker.Utilities;

import android.content.Context;
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

    //Generates the records into a CSV format
    public String getCSVFromRecords(ArrayList<Record> records) {
        String titles = "Medication Name,Dose,Time,Date \n";
        String csvFile = titles;

        for(int i=0; i<records.size(); i++) {
            Record record = records.get(i);
            String name = Utility.keyToName(record.getMedication_key());
            int dose = record.getDose();

            String time = record.getHour() + ":" +
                    record.getMinute() + ":" +
                    record.getSecond();

            String date = record.getDay() + "/" +
                    record.getMonth() + "/" +
                    record.getYear();

            csvFile = csvFile + name + "," + dose + "," + time + "," + date + "\n";
        }

        Log.d(TAG, csvFile);
        return csvFile;
    }

    //writes file to external storage
    public void writeFile(String content) {
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

    //emails file
    public Intent emailRecords() {
        File file = new File(Environment.getExternalStorageDirectory(), "/records.csv");
        Uri path = Uri.fromFile(file);
        Log.d(TAG, path.toString());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_SUBJECT, "Record of Doses Taken");
        intent.putExtra(Intent.EXTRA_TEXT, "Please find a csv attached.");
        intent.putExtra(Intent.EXTRA_STREAM, path);

        return intent;
    }

}
