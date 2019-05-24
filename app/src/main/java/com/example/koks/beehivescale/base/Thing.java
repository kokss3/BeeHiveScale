package com.example.koks.beehivescale.base;

public class Thing {

    private Integer id;
    private String unitName;
    private Float mass;
    private Float voltage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Float getMass() {
        return mass;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }

    public Float getVoltage() {
        return voltage;
    }

    public void setVoltage(Float voltage) {
        this.voltage = voltage;
    }

    @Override
    public String toString() {
        return "Thing{" +
                "id='" + id + "', " +
                "unitName='" + unitName + "', " +
                "mass=" + mass +
                ", voltage=" + voltage +
                '}';
    }
}
