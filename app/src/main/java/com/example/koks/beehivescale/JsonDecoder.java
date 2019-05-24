package com.example.koks.beehivescale;

import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.Thing;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class JsonDecoder {

    /**
     * inputs JSNObject and translates it to List of HashMap
     *
     * @return List<Map   <   String   ,       String>>>
     * objects to return are:
     * "unitname", "mass", "voltage"
     */
    public Dweet process(String obj) throws JSONException, ParseException {
        Dweet dweet = new Dweet();


        JSONObject jsonObj = new JSONObject(obj);
        JSONArray with = jsonObj.getJSONArray("with");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);

        dweet.setUnitDate(sdf.parse(with.getJSONObject(0).getString("created")));

        JSONObject a = with.getJSONObject(0).getJSONObject("content");

        Iterator<String> iter = a.keys();

        List<Thing> things = new ArrayList<>();
        while (iter.hasNext()) {

            Thing thing = new Thing();
            String name = iter.next();
            String value = a.getString(name);
            thing.setUnitName(name);
            thing.setVoltage(Float.valueOf(value.substring(0, value.indexOf(";"))));
            thing.setMass(Float.valueOf(value.substring(value.indexOf(";") + 1)));

            things.add(thing);
        }

        dweet.setUnits(things);

        return dweet;
    }
}
