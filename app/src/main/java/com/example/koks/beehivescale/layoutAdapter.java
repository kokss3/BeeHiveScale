package com.example.koks.beehivescale;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import android.graphics.Color;

import com.example.koks.beehivescale.base.DweetDatabase;

import static android.view.View.inflate;

public class layoutAdapter extends BaseAdapter {
    private TextView UnitID;
    private TextView MassValue;
    private TextView PercBatt;
    private ImageView Battery;
    private Context mContext;
    private View view;
    private ArrayList<ArrayList<String>> beeInfoList;
    private DweetDatabase database;

    public layoutAdapter(Context c, ArrayList<ArrayList<String>> beeInfoList) {

        this.beeInfoList = beeInfoList;
        this.mContext = c;
        view = inflate(mContext, R.layout.bee_adapter, null);
    }

    @Override
    public int getCount() {
        return beeInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return beeInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        database = new DweetDatabase(mContext);
        convertView = inflate(mContext, R.layout.bee_adapter, null);
        UnitID = convertView.findViewById(R.id.UnitID);
        MassValue = convertView.findViewById(R.id.mass_indicator);
        Battery = convertView.findViewById(R.id.statusBaterije);
        PercBatt = convertView.findViewById(R.id.postotakBatt);

        //set unitID to textView
        UnitID.setText("unit");
        MassValue.setText("Mass kg");


        double number = 3.9;
        int perc = (int) (number / 4.2) * 100;
        PercBatt.setText(String.valueOf(perc));
        number = number * 4.175;

        if (number>4){
            Battery.setImageResource(R.mipmap.zelena_puna);
        } else if (number<=4 && number > 3.8) {
            Battery.setImageResource(R.mipmap.zelena_skoropuna);
        } else if (number<=3.8 && number > 3.6) {
            Battery.setImageResource(R.mipmap.zelena_polupuna);
        } else if (number <= 3.6 && number > 3.12) {
            Battery.setImageResource(R.mipmap.zuta_pola);
        } else if (number <= 3.12) {
            PercBatt.setTextColor(Color.parseColor("#ff1818"));
            Battery.setImageResource(R.mipmap.crvana_prazno);
        }
        return convertView;
    }
}
