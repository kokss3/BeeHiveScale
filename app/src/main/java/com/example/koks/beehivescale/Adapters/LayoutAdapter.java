package com.example.koks.beehivescale.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import android.graphics.Color;

import com.example.koks.beehivescale.R;
import com.example.koks.beehivescale.base.Thing;

import static android.view.View.inflate;

public class LayoutAdapter extends BaseAdapter {
    private TextView UnitID;
    private TextView MassValue;
    private TextView PercBatt;
    private ImageView Battery;
    private Context mContext;
    private View view;
    private List<Thing> beeInfoList;
    LayoutInflater inflater;

    public LayoutAdapter(Context context, List<Thing> beeInfoList) {
        this.beeInfoList = beeInfoList;
        this.mContext = context;
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

        View newCV = convertView;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (newCV == null) {
            newCV = inflater.inflate(R.layout.bee_adapter, null);

            UnitID = newCV.findViewById(R.id.UnitID);
            MassValue = newCV.findViewById(R.id.mass_indicator);
            Battery = newCV.findViewById(R.id.statusBaterije);
            PercBatt = newCV.findViewById(R.id.postotakBatt);
        }
        //set unitID to textView
        if (beeInfoList.get(position).getAvatar() != null)
            UnitID.setText(beeInfoList.get(position).getAvatar());

        else UnitID.setText(beeInfoList.get(position).getUnitName());

        MassValue.setText(String.valueOf(beeInfoList.get(position).getMass()));

        double number = beeInfoList.get(position).getVoltage() / 255;
        int perc = (int) ((1 - ((4.2 - (number * 4.175)) / 1.2)) * 100);
        number = number * 4.175;

        PercBatt.setText(String.valueOf(perc));

        if (number>4){
            Battery.setImageResource(R.mipmap.zelena_puna);
        } else if (number<=4 && number > 3.8) {
            Battery.setImageResource(R.mipmap.zelena_skoropuna);
        } else if (number<=3.8 && number > 3.6) {
            Battery.setImageResource(R.mipmap.zelena_polupuna);
        } else if (number <= 3.6 && number > 3.2) {
            Battery.setImageResource(R.mipmap.zuta_pola);
        } else if (number <= 3.2) {
            PercBatt.setTextColor(Color.parseColor("#ff1818"));
            Battery.setImageResource(R.mipmap.crvana_prazno);
        }
        return newCV;
    }
}
