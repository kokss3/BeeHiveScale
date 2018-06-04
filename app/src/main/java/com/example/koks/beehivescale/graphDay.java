package com.example.koks.beehivescale;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

public class graphDay  extends DialogFragment {

    private String ID;
    private Long days = 1L;
    private HashMap<Date,String> graphBase;
    private HashMap<Date,HashMap<String,String>> finalBase = new HashMap<>();
    private static final String FILE_NAME_ADDRESS = "DateData.txt";
    private CheckBox thisDay, thisWeek, thisFortnight;
    Date timePeriod;

    GraphView graph;
    LineGraphSeries<DataPoint> series;

    public void setID(String ID) {
        this.ID = ID;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timePeriod = new Date(Calendar.getInstance().getTime().getTime() - (days *24*3600000)); // 7 * 24 * 60 * 60 * 1000

        System.out.println("timeline: " + (timePeriod.getTime()/(24*3600000)));
        try {
            FileInputStream fileInputStream = getActivity().openFileInput(FILE_NAME_ADDRESS);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            graphBase = (HashMap<Date, String>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.graph_day, container, false);
        thisDay = view.findViewById(R.id.thisDay);
        thisWeek = view.findViewById(R.id.thisWeek);
        thisFortnight = view.findViewById(R.id.thisFortnight);
        TextView Title = view.findViewById(R.id.name_for_day);
        Title.setText(ID);

        Set<Date> strings = graphBase.keySet();
        //here we make time interval to analyise

        //find in array how many keys are bigger than timePeriod
        //here goes Date keyset that opens and produces raw hashmap with id and mass
        ArrayList<Date> k = new ArrayList<>();
        for(Date a:strings){
            if(a.getTime()>timePeriod.getTime()){
            k.add(a);
            }
        }

        //create modified HashMap
        Gson gson = new Gson();
        HashMap<String, String> fields =new HashMap<>();
        try {
        for (int i = 0; i<k.size(); i++){
            fields = (HashMap<String, String>) gson.fromJson(graphBase.get(k.get(i)), HashMap.class);
            finalBase.put(k.get(i),fields);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DataPoint[] full = new DataPoint[finalBase.size()];

        for(int j = 0; j < finalBase.size(); j++){
            HashMap<String,String> tempi;
            tempi = finalBase.get(k.get(j));
            full[j] = new DataPoint(j,Integer.valueOf(tempi.get(ID)));
        }

        try {
            series = new LineGraphSeries<>(full);
            graph = view.findViewById(R.id.graph);

            thisDay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    graph.addSeries(series);
                }
            });
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMaxX(finalBase.size());
        }catch (Exception e){
            System.out.print(e);
        }

        thisDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days = 1L;
                thisWeek.setChecked(false);
                thisFortnight.setChecked(false);
                graph.addSeries(series);
            }
        });

        thisWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days = 7L;
                thisDay.setChecked(false);
                thisFortnight.setChecked(false);
                graph.addSeries(series);
            }
        });

        thisFortnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                days = 14L;
                thisWeek.setChecked(false);
                thisDay.setChecked(false);
                graph.addSeries(series);
            }
        });
        return view;
    }
}
