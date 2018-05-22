package com.example.koks.beehivescale;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class SavingFiles {
    Context mContext;

    //for txt format constructor
    public SavingFiles(Context context) {
        this.mContext = context;
    }

    public Object readObjectFile(String fileLocation) {
        Serializable obj = null;
        try {
            FileInputStream fileInputStream = mContext.openFileInput(fileLocation);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            obj = (Serializable) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception READ", e.getMessage());
        }
        return obj;
    }

    public void saveObjectFile(String fileLocation, HashMap obj) {

        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(fileLocation, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(obj);
            //fileOutputStream.close();
            objectOutputStream.close();
        } catch (Exception e) {
            Log.e("SaveFileExeption", String.valueOf(e));
        }
    }

    public String readFile(String fileLocation) {
        String temp = "";
        int c;
        try {
            FileInputStream fin = mContext.openFileInput(fileLocation);
            while ((c = fin.read()) != -1) {
                temp = temp + Character.toString((char) c);
            }
        } catch (Exception e) {
        }

        return temp;
    }

    public void saveFile(String fileLocation, String txtToWrite) {
        try {
            FileOutputStream fOut = mContext.openFileOutput(fileLocation, MODE_PRIVATE);
            fOut.write(txtToWrite.getBytes());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
