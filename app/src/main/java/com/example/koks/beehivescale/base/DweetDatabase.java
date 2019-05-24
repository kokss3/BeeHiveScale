package com.example.koks.beehivescale.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
            DWEET_UNIT_ID + " varchar(32) primary key not null unique, " +
            DWEET_DATE + " datetime)";

    private final static String EXEC_THINGS = "create table " +
            THING_TABLE + "(" +
            THING_UNIT_ID + " varchar(32)," +

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
        db.execSQL(EXEC_VALUE);
        db.execSQL(EXEC_THINGS);
        db.execSQL(EXEC_AVATAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Dweet getDweet(Date date) throws ParseException {
        String query = "select " +
                THING_TABLE + "." +
                THING_VOLTAGE + ", " +
                THING_TABLE + "." +
                THING_MASS + ", " +
                AVATAR_TABLE + "." +
                AVATAR_AVATAR + ", " +
                AVATAR_TABLE + "." +
                AVATAR_UNIT_ID + " from " +
                THING_TABLE + " inner join " +
                AVATAR_TABLE + " on " +
                THING_TABLE + "." +
                THING_UNIT_NAME + " = " +
                AVATAR_TABLE + "." +
                AVATAR_UNIT_ID + " where " +
                DWEET_DATE + " = " + date.toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SQLiteDatabase db = this.getReadableDatabase();
        Dweet dweet = new Dweet();

        Cursor cd1 = db.rawQuery(query, null);

        Cursor cd = db.query(THING_TABLE, new String[]{THING_MASS, THING_VOLTAGE, AVATAR_UNIT_ID, AVATAR_AVATAR, DWEET_DATE}, null, null, null, null, null);


        return dweet;
    }

    public void insertDweet(Dweet dweet) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DWEET_DATE, dweet.getUnitDate().toString());
        db.insert(DWEET_TABLE, null, contentValues);

        db.close();
    }

    public void insertThing(Dweet dweet) throws SQLiteConstraintException {
        ContentValues contentValues = new ContentValues();
        List<Thing> things = dweet.getUnits();
        SQLiteDatabase db = this.getWritableDatabase();

        for (Thing thing : things) {
            contentValues.put(THING_UNIT_ID, thing.getId());
            contentValues.put(THING_UNIT_NAME, thing.getUnitName());
            contentValues.put(THING_MASS, thing.getMass());
            contentValues.put(THING_VOLTAGE, thing.getVoltage());

            db.insert(THING_TABLE, null, contentValues);
        }
        db.close();
    }

    public List<String> getUnits() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> unitNames = new ArrayList<>();

        db.close();
        return unitNames;
    }

    private Boolean isColumnPresent(String columnName) throws SQLiteException {
        SQLiteDatabase db = this.getReadableDatabase();

        return true;

    }

    public Boolean checkIfDateExist(Date testDate) {
        return true;
    }
}
