package com.example.koks.beehivescale;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.DweetDatabase;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

//here we get all data from dweet.io every 30 minutes
public class NetGetterService extends Service {
    private String rawData="none";
    JsonDecoder decoder = new JsonDecoder();
    private static final String API_LINK = "https://dweet.io/get/latest/dweet/for/";
    private static final String API_LINK_5_DWEETS = "https://dweet.io/get/dweets/for/";
    private Long timeToWait = 30L; //in minutes

    @Override
    public void onCreate() {
        Thread getter = new Thread() {

            @Override
            public void run() {
                DweetDatabase dataBase = new DweetDatabase(getApplicationContext());
                int counter = 0;
                try {
                    while (true) {

                        //get remembered dweet name
                        String thingName = dataBase.getCredentials();
                        if (thingName != null)
                            rawData = getHTTPData(API_LINK + thingName);
                        else
                            rawData = getHTTPData(API_LINK + "testis");

                        try {
                            if (counter >= 5) {
                                counter = 0;

                                List<Dweet> dweetList = decoder.proccessFiveDweets(getHTTPData(
                                        API_LINK_5_DWEETS + thingName));

                                if (dweetList != null) {
                                    for (Dweet dt : dweetList) {
                                        if (!dataBase.checkIfDateExist(dt.getUnitDate())) {
                                            dataBase.insertDweet(dt);
                                            dataBase.insertThing(dt, dataBase.getId(dt.getUnitDate()));
                                        }
                                    }
                                }
                            }

                            Dweet dweet = decoder.processOneDweet(rawData);

                            if (!dataBase.checkIfDateExist(dweet.getUnitDate())) {
                                dataBase.insertDweet(dweet);
                                dataBase.insertThing(dweet, dataBase.getId(dweet.getUnitDate()));
                            }

                            counter++;
                            Thread.sleep((timeToWait * 60 * 1000));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        getter.start();
    }

    private String getHTTPData(String urlStream) {
        String stream = "";

        try {
            URL url = new URL(urlStream);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() == 200) {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    stream = sb.toString();
                }
            }

            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stream;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
        }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
