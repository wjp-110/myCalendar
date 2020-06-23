package com.werb.mycalendardemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyOpenHelper extends SQLiteOpenHelper {
    private  final static String NAME = "calendar.db";
    private final static int VEERSION = 1;
    public MyOpenHelper(Context context) {
        super(context, NAME, null, VEERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table alarmlist(_id integer primary key autoincrement,title char(20),isAllday int(20)," +
                "isVibrate int(20),year int(20),month int(20),day int(20),startTimeHour int(20),startTimeMinute int(20),"+
        "endTimeHour int(20),endTimeMinute int(20),alarmTime char(20),alarmColor char(20),alarmTonePath char(20),local char(20),"+
        "description char(100),replay char(20))");
        db.execSQL("create table user(biaoqian varchar(64),myname varchar(64),banji varchar(64),username String primary key,psw varchar(64))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
