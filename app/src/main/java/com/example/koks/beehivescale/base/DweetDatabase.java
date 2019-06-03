package com.example.koks.beehivescale.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DweetDatabase extends SQLiteOpenHelper {

    private final static int VERSION = 1;

    private final static String DATABASE_NAME = "DweetDB.db";

    private final static String NAME_DWEET = "name_dweet";
    private final static String NAME_THING = "name_thing";

    private final static String DWEET_TABLE = "dweet_table";
    private final static String DWEET_UNIT_ID = "id";
    private final static String DWEET_DATE = "date";

    private final static String THING_TABLE = "thing_table";
    private final static String THING_UNIT_ID = "id";
    private final static String THING_UNIT_NAME = "unit_id";
    private final static String THING_MASS = "mass";
    private final static String THING_VOLTAGE = "voltage";

    private final static String AVATAR_TABLE = "avatar_table";
    private final static String AVATAR_UNIT_ID = "unit_id";
    private final static String AVATAR_AVATAR = "avatar";


    private final static String EXEC_VALUE = "create table " +
            DWEET_TABLE + "(" +
            DWEET_UNIT_ID + " integer primary key autoincrement, " +
            DWEET_DATE + " datetime)";


    private final static String EXEC_NAME = "create table " +
            NAME_DWEET + "(" +
            NAME_THING + " varchar(32)) ";

    private final static String EXEC_THINGS = "create table " +
            THING_TABLE + "(" +
            THING_UNIT_ID + " integer," +

            THING_UNIT_NAME + " varchar(32), " +
            THING_MASS + " float(6,1), " +
            THING_VOLTAGE + " float(2,2), foreign key(" +
            THING_UNIT_ID + ") references " +
            DWEET_TABLE + "(" +
            DWEET_UNIT_ID + "), foreign key (" +
            THING_UNIT_NAME + ") references " +
            AVATAR_TABLE + "(" +
            AVATAR_UNIT_ID + "))";

    private final static String EXEC_AVATAR = "create table " +
            AVATAR_TABLE + "(" +
            AVATAR_UNIT_ID + " varchar(32), " +
            AVATAR_AVATAR + " varchar(255))";

    public DweetDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXEC_NAME);
        db.execSQL(EXEC_VALUE);
        db.execSQL(EXEC_THINGS);
        db.execSQL(EXEC_AVATAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EXEC_NAME);

    }

    public Dweet getLastDweet() {
        System.out.println("GetLastDweet");

        Dweet dweet = new Dweet();
        SQLiteDatabase db = this.getReadableDatabase();
        List<Thing> thingList = new ArrayList<>();

        String getDateAndID = "select * from dweet_table order by id desc limit 1";

        Cursor date = db.rawQuery(getDateAndID, null);

        if (date != null) {
            while (date.moveToNext()) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                try {
                    dweet.setUnitDate(sdf.parse(date.getString(date.getColumnIndex(DWEET_DATE))));
                    dweet.setId(date.getInt(date.getColumnIndex(DWEET_UNIT_ID)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        // select date from dweet_table order by id desc LIMIT 1;
        String query = "select * from " + THING_TABLE + " where " + THING_UNIT_ID + "=? ";

        Cursor things = db.rawQuery(query, new String[]{String.valueOf(dweet.getId())});

        if (things != null) {
            while (things.moveToNext()) {
                Thing thing = new Thing();
                thing.setId(things.getInt(things.getColumnIndex(THING_UNIT_ID)));
                thing.setMass(things.getFloat(things.getColumnIndex(THING_MASS)));
                thing.setVoltage(things.getFloat(things.getColumnIndex(THING_VOLTAGE)));
                thing.setUnitName(things.getString(things.getColumnIndex(THING_UNIT_NAME)));
                thingList.add(thing);
            }
        }
        System.out.println("ThingList " + thingList);
        dweet.setUnits(thingList);

        String newQuery = "select " + AVATAR_AVATAR + " from " + AVATAR_TABLE + " where unit_id=?";

        for (int i = 0; i < thingList.size(); i++) {
            Cursor avatar = db.rawQuery(newQuery, new String[]{thingList.get(i).getUnitName()});

            if (avatar != null) {
                while (avatar.moveToNext()) {
                    thingList.get(i).setAvatar(avatar.getString(avatar.getColumnIndex(AVATAR_AVATAR)));
                }
            }
            avatar.close();
        }

        System.out.println("Last Dweet is: " + dweet);
        things.close();
        db.close();
        return dweet;
    }

    public Dweet getDweet(Date date) throws IllegalStateException, NullPointerException {
        Dweet dweet = new Dweet();
        dweet.setId(getId(date));
        dweet.setUnitDate(date);
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Thing> thingList = new ArrayList<>();

        String query = "select * from " + THING_TABLE + " where id=?";

        Cursor things = db.rawQuery(query, new String[]{dweet.getId().toString()});

        if (things != null) {
            while (things.moveToNext()) {
                Thing thing = new Thing();
                thing.setId(things.getInt(things.getColumnIndex(THING_UNIT_ID)));
                thing.setMass(things.getFloat(things.getColumnIndex(THING_MASS)));
                thing.setVoltage(things.getFloat(things.getColumnIndex(THING_VOLTAGE)));
                thing.setUnitName(things.getString(things.getColumnIndex(THING_UNIT_NAME)));

                thingList.add(thing);
            }
        }

        for (int i = 0; i < thingList.size(); i++) {
            String newQuery = "select " + AVATAR_AVATAR + " from " + AVATAR_TABLE + " where unit_id=?";

            Cursor avatar = db.rawQuery(newQuery, new String[]{thingList.get(i).getUnitName()});

            if (avatar != null) {
                while (avatar.moveToNext()) {
                    thingList.get(i).setAvatar(avatar.getString(avatar.getColumnIndex(AVATAR_AVATAR)));
                }
            }
            avatar.close();
        }

        dweet.setUnits(thingList);
        things.close();
        db.close();
        return dweet;
    }


    public void deleteAvatar(Thing thing) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + AVATAR_TABLE + " where " + AVATAR_UNIT_ID + " = '" + thing.getUnitName() + "';");

        db.close();
    }

    public void insertAvatar(Thing thing) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(AVATAR_UNIT_ID, thing.getUnitName());
        contentValues.put(AVATAR_AVATAR, thing.getAvatar());

        db.insert(AVATAR_TABLE, null, contentValues);

        db.close();
    }

    public void insertDweet(Dweet dweet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DWEET_DATE, dweet.getUnitDate().toString());
        db.insert(DWEET_TABLE, null, contentValues);

        db.close();
    }

    public void insertThing(Dweet dweet, int id) throws SQLiteConstraintException {
        ContentValues contentValues = new ContentValues();
        List<Thing> things = dweet.getUnits();
        SQLiteDatabase db = this.getWritableDatabase();

        for (Thing thing : things) {
            contentValues.put(THING_UNIT_ID, id);
            contentValues.put(THING_UNIT_NAME, thing.getUnitName());
            contentValues.put(THING_MASS, thing.getMass());
            contentValues.put(THING_VOLTAGE, thing.getVoltage());

            db.insert(THING_TABLE, null, contentValues);
        }
        db.close();
    }

    //getting unit id by specific date
    public Integer getId(Date date) throws IllegalArgumentException, NullPointerException {
        int id = 0;
        int index;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cd = null;

        cd = db.rawQuery("SELECT " + DWEET_UNIT_ID + " FROM " + DWEET_TABLE + " where date=?", new String[]{date.toString()});

        if (cd != null) {
            index = cd.getColumnIndex(DWEET_UNIT_ID);
        } else {
            System.out.println("Index is hardcoded");
            index = 0;
        }

        while (cd.moveToNext()) {
            id = cd.getInt(index);
        }

        cd.close();
        db.close();

        return id;
    }

    public Boolean checkIfDateExist(Date testDate) {
        int i = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cd = null;

        try {
            cd = db.rawQuery("SELECT " + DWEET_DATE + " FROM " + DWEET_TABLE + " where date=?", new String[]{testDate.toString()});
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        while (cd.moveToNext()) {
            i = cd.getColumnCount();
        }

        cd.close();
        db.close();

        return (i > 0);
    }

    public void insertCreds(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME_THING, name);
        db.insert(NAME_DWEET, null, contentValues);

        db.close();
    }

    public String getCreds() {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "select * from " + NAME_DWEET;
        Cursor things = db.rawQuery(query, null);

        if (things != null) {
            while (things.moveToNext()) {
                name = things.getString(things.getColumnIndex(NAME_THING));
            }
        }

        db.close();
        return name;
    }

}
