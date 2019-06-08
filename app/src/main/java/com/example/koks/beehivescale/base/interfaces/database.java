package com.example.koks.beehivescale.base.interfaces;

import android.database.sqlite.SQLiteConstraintException;

import com.example.koks.beehivescale.base.Dweet;
import com.example.koks.beehivescale.base.Thing;

import java.util.Date;
import java.util.List;

public interface database {

    int VERSION = 1;

    String DATABASE_NAME = "DweetDB.db";

    String NAME_DWEET = "name_dweet";
    String NAME_THING = "name_thing";

    String DWEET_TABLE = "dweet_table";
    String DWEET_UNIT_ID = "id";
    String DWEET_DATE = "date";

    String THING_TABLE = "thing_table";
    String THING_UNIT_ID = "id";
    String THING_UNIT_NAME = "unit_id";
    String THING_MASS = "mass";
    String THING_VOLTAGE = "voltage";

    String AVATAR_TABLE = "avatar_table";
    String AVATAR_UNIT_ID = "unit_id";
    String AVATAR_AVATAR = "avatar";

    Dweet getDweet(Date date) throws IllegalStateException, NullPointerException;

    Dweet getLastDweet();

    String getCredentials();

    Integer getId(Date date) throws IllegalArgumentException, NullPointerException;

    void insertThing(Dweet dweet, int id) throws SQLiteConstraintException;

    void insertDweet(Dweet dweet);

    void insertAvatar(Thing thing);

    void insertCredentials(String name);

    void deleteAvatar(Thing thing);

    List<Date> getDates(Date dateBegin, Date dateEnd);

    Date getSpecificDayBefore(Date startDate, int daysBefore);

    Boolean checkIfDateExist(Date testDate);
}
