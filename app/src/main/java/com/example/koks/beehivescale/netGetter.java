package com.example.koks.beehivescale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class netGetter {
    boolean hasConnection;
    private Context _context;
    private static String stream;

    public netGetter(Context _context) {
        this._context = _context;
    }

    public boolean itHasConnection() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public String getHTTPData(String urlStream) {

        hasConnection = false;
        try {
            URL url = new URL(urlStream);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if (httpURLConnection.getResponseCode() == 200) {
                hasConnection = true;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    stream = sb.toString();
                }
            } else {
                //here we will run saved backup
                hasConnection = false;
            }
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error: "+ e);
        }
        return stream;
    }
}
