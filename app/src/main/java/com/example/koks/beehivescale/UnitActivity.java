package com.example.koks.beehivescale;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.koks.beehivescale.Adapters.ThingsAdapter;
import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.DweetDatabase;

public class UnitActivity extends AppCompatActivity {
    DweetDatabase database;
    Button datePicker;
    ListView thingList;
    TextView unitAvatar, chosenDate, unitRealName;
    String unitName, clickedDate;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.unit_activity);
        unitName = getIntent().getExtras().getString("unitName");
        database = new DweetDatabase(this);

        unitRealName = findViewById(R.id.unitName);
        unitAvatar = findViewById(R.id.avatar);
        chosenDate = findViewById(R.id.chosenDate);
        thingList = findViewById(R.id.list_things);
        datePicker = findViewById(R.id.pickDate);

        String avatar = database.getAvatar(unitName);

        if (avatar.equals("")) avatar = unitName;

        unitAvatar.setText(avatar);
        unitRealName.setText(unitName);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        if (savedInstanceState != null) {
            clickedDate = savedInstanceState.getString("clickedDate");
            getAdapter(clickedDate);
        }

        datePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog =
                    new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
                        clickedDate = year + "-" +  (monthOfYear + 1) + "-" + dayOfMonth;
                        getAdapter(clickedDate);
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
    }

    private void getAdapter(String datePicked) {
        List<Date> datesForDay;
        List<Dweet> dweetsForDay = new ArrayList<>();
        Date selectedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
        try {
            selectedDate = sdf.parse(datePicked);
            System.out.println("Date grr: "+ selectedDate);
            chosenDate.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH).format(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        datesForDay = database.getDates(selectedDate);
        for (Date date : datesForDay) dweetsForDay.add(database.getDweet(date));

        Collections.sort(dweetsForDay,(o1, o2) ->  o1.getUnitDate().compareTo(o2.getUnitDate()));

        ThingsAdapter thingAdapter = new ThingsAdapter(this, dweetsForDay, unitName);
        thingList.setAdapter(thingAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("clickedDate", clickedDate);
        super.onSaveInstanceState(outState);
    }
}
