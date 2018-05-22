package com.example.koks.beehivescale;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;

public class SaveBaseInfoData {
    private static final String FILENAME_BACKUP = "dataraw.txt";
    private static final String FILENAME_THING = "thingBackup.txt";
    String string;
    private Context mContext;

    public String readFile() {
        return string;
    }

    public void writeFile(String input) {
        File file = new File(mContext.getFilesDir() + FILENAME_BACKUP);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(input.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}