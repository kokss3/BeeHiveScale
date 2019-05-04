package com.example.koks.beehivescale;

import com.example.koks.beehivescale.base.Dweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JsonDecoder {

    /**
     * inputs JSNObject and translates it to List of HashMap
     *
     * @return List<Map   <   String   ,       String>>>
     * objects to return are:
     * "unitname", "mass", "voltage"
     */
    public List<Dweet> process(String obj) throws JSONException, ParseException {
        List<Dweet> list = new ArrayList();
        Dweet dweet = new Dweet();

        JSONObject jsonObj = new JSONObject(obj);
        JSONArray with = jsonObj.getJSONArray("with");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        dweet.setUnitDate(sdf.parse(with.getJSONObject(0).getString("created")));

        System.out.println(dweet.getUnitDate());

        JSONObject a = with.getJSONObject(0).getJSONObject("content");

        Iterator<String> iter = a.keys();

        while (iter.hasNext()) {
            Dweet tempDweet = new Dweet();

            tempDweet.setUnitDate(dweet.getUnitDate());

            tempDweet.setUnitName(iter.next());

            String value = a.getString(tempDweet.getUnitName());

            tempDweet.setUnitVoltage(value.substring(0, value.indexOf(";")));

            tempDweet.setUnitMass(value.substring(value.indexOf(";") + 1));
            list.add(tempDweet);
        }
        return list;
    }
}
