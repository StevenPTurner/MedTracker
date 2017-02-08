package com.medtracker.Utilities;

/**
 * Created by spt10 on 01/02/2017.
 */

public class Utility {

    public static String nameToKey(String name) {
        String key = name.toLowerCase();
        key = key.replaceAll(" ","_");

        return key;
    }

    public static String keyToName(String key) {
        String name = "";
        String[] toUpCase = key.split("_");

        for (int i=0; i< toUpCase.length; i++) {
            name = name +  toUpCase[i].substring(0,1).toUpperCase() +
                    toUpCase[i].substring(1).toLowerCase() +
                    " ";
        }
        name = name.trim();
        return name;
    }



}
