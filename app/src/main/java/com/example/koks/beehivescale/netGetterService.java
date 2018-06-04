package com.example.koks.beehivescale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//here we get all data from dweet.io every 30 minutes
public class netGetterService extends Service {

    private String rawData="none";
    private Long timeToWait = 30L; //in minutes

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread getter = new Thread() {
            boolean hasConnection;
            netGetter retrieverOfData = new netGetter(getApplicationContext());
            Common dataForDweet = new Common(getApplicationContext());

            @Override
            public void run() {
                while (true) {
                    try {
                        System.out.println("Sleep for " + timeToWait + " mins!");

                        Thread.sleep((timeToWait*60000));
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }

                rawData = retrieverOfData.getHTTPData(dataForDweet.apiRequestNotKeyed());
                System.out.println("Out stream: "+ rawData);
                    String timestamp= "";

                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(rawData);

                    JSONArray with = jsonObj.getJSONArray("with");
                    JSONObject content = with.getJSONObject(0);
                        //get time string for further archiving
                        //for now it is just local variable with nothing to do
                        timestamp = content.toString();
                        timestamp = timestamp.substring(timestamp.indexOf("created\":\""), timestamp.indexOf("Z")-4);
                        timestamp = timestamp.replace("created\":\"","");
                        Log.e("Time of recovery",timestamp);

                    } catch (JSONException e) {
                    e.printStackTrace();
                }

                    //start backup
                    createJSON takeInput = new createJSON(getApplicationContext(), timestamp);
                    takeInput.run();

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
