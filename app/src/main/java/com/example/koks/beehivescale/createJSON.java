package com.example.koks.beehivescale;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class createJSON extends Thread {
    private String date;
    private static final String FILE_NAME_ADDRESS = "DateData.txt";

    private Context mContext;
    private HashMap<Date,String> finalBase;
    private HashMap<String,String> allUnits = new HashMap<>();
    private Date NEWdate;

    createJSON(Context c, String date) {
        mContext = c;
        this.date = date;
    }

    public void run() {
        finalBase = new HashMap<>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            NEWdate = format.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fileInputStream = mContext.openFileInput(FILE_NAME_ADDRESS);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            finalBase = (HashMap<Date, String>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            System.out.println(finalBase.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error Reading",e.getMessage());
        }
    }

    void putUnitData (String ID, String mass){
        //put all units no mater how many there are in one hour
        allUnits.put(ID,mass);
    }

    void saveLists(){

        //check if it is stored already
        //if not, store it
        try{
        if(!finalBase.containsKey(NEWdate)) {

            JSONObject json = new JSONObject(allUnits);
            finalBase.put(NEWdate,json.toString());
            System.out.println(finalBase.toString());

            allUnits.clear();
        }
    }catch(Exception e){
            e.printStackTrace();
        }
        saveJSONtoFile();
    }

    //save this file in Json base on internal memory
    //and clear old base
    void saveJSONtoFile (){
        try {
            FileOutputStream fileOutputStream = mContext.openFileOutput(FILE_NAME_ADDRESS, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(finalBase);
            objectOutputStream.close();
            fileOutputStream.close();
            finalBase.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
