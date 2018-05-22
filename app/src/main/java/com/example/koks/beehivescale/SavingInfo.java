package com.example.koks.beehivescale;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Properties;

public class SavingInfo {
    private final static String FILE_NAME_ADDRESS = "base_map_backup.txt";
    private Context mContext;
    private HashMap<String,String> partOne = new HashMap<>();

    public SavingInfo(Context context) {
        this.mContext = context;
    }

    private HashMap<String,String> readFromFile(String location) {
        HashMap<String,String> part = new HashMap<>();

        try {
            FileInputStream fileInputStream = mContext.openFileInput(location);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            part = (HashMap<String, String>)objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            Log.e("Fault",e.getMessage());
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        return part;
    }
    //moram si izvuči iz filea cijeli Map
    //i onda u mapi naći koji kwy želim replaceati
    public void addNewUnitID(String original, String replaced) {
        partOne.putAll(readFromFile(FILE_NAME_ADDRESS));
        partOne.put(original,replaced);

        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_NAME_ADDRESS, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(partOne);
            objectOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readNewUnitID(String original) {
        String out = original;

        try {
            if (readFromFile(FILE_NAME_ADDRESS).containsKey(original)) {
                out = readFromFile(FILE_NAME_ADDRESS).get(original);
            }else {
                out = original;

                Log.e("renamer of unitID","Don't have");
            }
            return out;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ReadExeption", String.valueOf(e));
        }
        return out;
    }
}