package com.example.koks.beehivescale.base;

import java.util.Date;
import java.util.List;

public class Dweet {
    private List<Thing> units;
    private Date unitDate;

    public Dweet() {
        super();
    }

    public Dweet(List<Thing> things, Date unitDate) {
        this.unitDate = unitDate;
        this.units = things;
    }

    public List<Thing> getUnits() {
        return units;
    }

    public void setUnits(List<Thing> units) {
        this.units = units;
    }

    public Date getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(Date unitDate) {
        this.unitDate = unitDate;
    }

    @Override
    public String toString() {
        return "Dweet{" +
                "units=" + units +
                ", unitDate=" + unitDate +
                '}';
    }
}
