package com.example.koks.beehivescale;


public class jsonValues {
    private String units;
    private String[] unitWithValues;

    public jsonValues(String units) {
        this.units = units;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String[] getUnitWithValues() {
        return unitWithValues;
    }

    public void setUnitWithValues(String[] unitWithValues) {
        this.unitWithValues = unitWithValues;
    }

    public jsonValues(String units, String[] unitWithValues) {
        this.units = units;
        this.unitWithValues = unitWithValues;
    }
}
