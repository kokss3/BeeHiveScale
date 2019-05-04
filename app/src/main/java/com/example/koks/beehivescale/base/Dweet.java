package com.example.koks.beehivescale.base;

import java.util.Date;

public class Dweet {
    private String unitName;
    private String unitMass;
    private String unitAvatar;
    private String unitVoltage;
    private Date unitDate;


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitMass() {
        return unitMass;
    }

    public void setUnitMass(String unitMass) {
        this.unitMass = unitMass;
    }

    public String getUnitAvatar() {
        return unitAvatar;
    }

    public void setUnitAvatar(String unitAvatar) {
        this.unitAvatar = unitAvatar;
    }

    public Date getUnitDate() {
        return unitDate;
    }

    public void setUnitDate(Date unitDate) {
        this.unitDate = unitDate;
    }

    public String getUnitVoltage() {
        return unitVoltage;
    }

    public void setUnitVoltage(String unitVoltage) {
        this.unitVoltage = unitVoltage;
    }
}
