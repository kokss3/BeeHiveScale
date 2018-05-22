package com.example.koks.beehivescale;

import java.util.ArrayList;

public class SingleRow {
    private String unit;
    private float Mass;
    private float Volts;

    public SingleRow(ArrayList<String> partialArray) {
        this.unit = partialArray.get(0);
        this.Mass = Float.parseFloat(partialArray.get(1));
        this.Volts = Float.parseFloat(partialArray.get(2));
    }

    public String getUnit() {
        return unit;
    }

    public float getMass() {
        return Mass;
    }

    public float getVolts() {
        return Volts;
    }
}