package com.example.koks.beehivescale.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DweetDatabase extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "DweetDB.db";
    private final static String TABLE_DWEET = "dweet_table";
    private final static String COLUMN_ID = "date";
    private final static String COLUMN_NAME = "name";
    private final static String COLUMN_MASS = "mass";
    private final static String TABLE_THING = "thing_table";
    private final static String COLUMN_UNIT_ID = "unit_name";
    private final static String COLUMN_AVATAR = "avatar";
    public final static String EXEC_UNITS = "create table " +
            TABLE_THING + "(" +
            COLUMN_UNIT_ID + " varchar(32) primary key, " +
            COLUMN_AVATAR + " varchar(255));";
    private final static int VERSION = 3;

    private final static String EXEC_VALUE = "create table " +
            TABLE_DWEET + "(" +
            COLUMN_ID + " datetime primary key not null, " +
            COLUMN_NAME + " varchar(32), " +
            COLUMN_MASS + " float(6,1)) ";
    private Dweet dweet = new Dweet();

    public DweetDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXEC_VALUE);
        db.execSQL(EXEC_UNITS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //String modifyTable = "alter table "+TABLE_DWEET+ "add "+dweet.getThings().get("unit")+" varchar(64)";
        //db.execSQL(modifyTable);
    }

    public void insertThing(List<Dweet> dweet) {
        SQLiteDatabase db = this.getWritableDatabase();
        List<String> names = new ArrayList<>();
        ContentValues contentValues = new ContentValues();

        for (Dweet dt : dweet)
            contentValues.put(COLUMN_UNIT_ID, dt.getUnitName());

        db.insert(TABLE_THING, null, contentValues);
        db.close();
    }

    public void insertThing(String unitID, String avatar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_UNIT_ID, unitID);
        contentValues.put(COLUMN_AVATAR, avatar);
        db.insert(TABLE_THING, null, contentValues);
        db.close();
    }

    public void insertDweet(List<Dweet> dweets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (Dweet dweet : dweets) {
            contentValues.put(COLUMN_ID, dweet.getUnitDate().toString());
            contentValues.put(COLUMN_MASS, dweet.getUnitMass());
        }
        db.insert(TABLE_DWEET, null, contentValues);
        db.close();
    }

    public Date retriveDate(Date testDate) throws ParseException {
        SQLiteDatabase db = this.getReadableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Cursor cd = db.query(TABLE_THING, null,
                null, null, null, null, null);

        String date = "";
        while (cd.moveToNext()) {
            int cid = cd.getColumnIndex(testDate.toString());
            date = cd.getString(cid);
        }
        db.close();

        return dateFormat.parse(date);
    }
}
