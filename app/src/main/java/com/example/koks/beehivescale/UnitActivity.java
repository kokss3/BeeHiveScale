package com.example.koks.beehivescale;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.koks.beehivescale.Adapters.ThingsAdapter;
import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.DweetDatabase;

public class UnitActivity extends AppCompatActivity {
    DweetDatabase database;
    Button datePicker;
    boolean emptyPicker;
    ListView thingList;
    TextView unitAvatar, chosenDate;
    List<Dweet> dweetsForDay;
    List<Date> datesForDay;
    String unitName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_activity);
        emptyPicker = false;
        database = new DweetDatabase(this);
        unitName = getIntent().getExtras().getString("unitName");
        dweetsForDay = new ArrayList<>();

        unitAvatar = findViewById(R.id.unitName);
        chosenDate = findViewById(R.id.chosenDate);
        thingList = findViewById(R.id.list_things);
        datePicker = findViewById(R.id.pickDate);
        unitAvatar.setText(unitName);

        datePicker.setOnClickListener(v ->
                getAdapter(database.getLastDweet().getUnitDate())
        );
    }

    private void getAdapter(Date datePicked) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        chosenDate.setText(sdf.format(datePicked));

        datesForDay = database.getDates(
                database.getSpecificDayBefore(datePicked, 1), datePicked);

        for (Date date : datesForDay) dweetsForDay.add(database.getDweet(date));

        System.out.println(datePicked);
        ThingsAdapter thingAdapter = new ThingsAdapter(this, dweetsForDay, unitName);
        thingList.setAdapter(thingAdapter);
    }
}
