package com.example.koks.beehivescale.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.koks.beehivescale.R;
import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.Thing;

import java.text.DateFormat;
import java.util.List;

public class ThingsAdapter extends BaseAdapter {

    private final List<Dweet> dweetList;
    private final Context mContext;
    private final String unitName;

    public ThingsAdapter(Context context, List<Dweet> list, String name) {
        this.mContext = context;
        this.dweetList = list;
        this.unitName = name;
    }

    @Override
    public int getCount() {
        return dweetList.size();
    }

    @Override
    public Object getItem(int position) {
        return dweetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        Dweet dt = dweetList.get(position);

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.unit_dweet, null);
        TextView massShow = convertView.findViewById(R.id.mass);
        TextView dateShow = convertView.findViewById(R.id.date);

        for (Thing asd : dt.getUnits()) {
            if (asd.getUnitName().equals(unitName)) {
                String mass = asd.getMass() + " kg";
                massShow.setText(mass);
                dateShow.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(dt.getUnitDate()));
            }
        }
        return convertView;
    }
}
