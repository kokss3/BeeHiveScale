package com.example.koks.beehivescale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.DweetDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//here we get all data from dweet.io every 30 minutes
public class netGetterService extends Service {
    private String rawData="none";
    JsonDecoder decoder = new JsonDecoder();
    private Long timeToWait = 5L; //in minutes

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread getter = new Thread() {
            netGetter retrieverOfData = new netGetter(getApplicationContext());
            Common dataForDweet = new Common();

            @Override
            public void run() {
                DweetDatabase dataBase = new DweetDatabase(getApplicationContext());
                while (true) {
                    rawData = retrieverOfData.getHTTPData(dataForDweet.apiRequestNotKeyed());

                    try {

                        List<Dweet> dweetList = decoder.process(rawData);
                        dataBase.insertThing(dweetList);
                        dataBase.insertDweet(dweetList);

                        System.out.println("Sleep for " + timeToWait + " mins!");
                        Thread.sleep((timeToWait * 60 * 1000));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        getter.start();
        return START_STICKY;
        }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
