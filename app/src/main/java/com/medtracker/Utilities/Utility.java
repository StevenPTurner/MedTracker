package com.medtracker.Utilities;

/**
 * Created by spt10 on 01/02/2017.
 */

public class Utility {

    public static String parseKey(String toParse) {
        toParse = toParse.toLowerCase();
        toParse = toParse.replaceAll(" ","_");

        return toParse;
    }

}
