package com.example.koks.beehivescale;

import android.content.Context;

public class Common {
    private static String MY_THING = "";
    private static String API_KEY = "";
    private static final String API_LINK = "https://dweet.io/get/latest/dweet/for/";
    private static final String FILE_NAME_ADDRESS_ITEM = "Settings_info_thing.txt";
    private static final String FILE_NAME_ADDRESS_KEY = "Settings_info_key.txt";

    public Common(Context context) {
        SavingFiles fileSaver = new SavingFiles(context);

        if (fileSaver.readFile(FILE_NAME_ADDRESS_ITEM) == "") {
            MY_THING = "testis2";
        }

        MY_THING = fileSaver.readFile(FILE_NAME_ADDRESS_ITEM);
        API_KEY = fileSaver.readFile(FILE_NAME_ADDRESS_KEY);
    }

    public void changeThing(String newThing) {
        MY_THING = newThing;
    }

    public void changeKey(String newKey) {
        API_KEY = newKey;
    }

    public static String apiRequestNotKeyed() {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(MY_THING);
        return sb.toString();
    }

    public static String apiRequestKeyed() {
        StringBuilder sb = new StringBuilder(API_LINK);
        sb.append(String.format(MY_THING, "?key=", API_KEY));
        return sb.toString();
    }
}
