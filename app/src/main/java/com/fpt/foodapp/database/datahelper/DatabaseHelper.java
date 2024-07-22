package com.fpt.foodapp.database.datahelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_FOOD = "FoodAppV2.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_FOOD, null, DATABASE_VERSION);
    }

    //Tạo bảng OderDetail;
    static final String CREATE_TABLE_ODER_DETAIL = "CREATE TABLE OderDetail( iD INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE ,Image TEXT , ProductId TEXT , ProductName TEXT , QuanTity TEXT ,Price TEXT ,Discount TEXT) ";




    @Override
    public void onCreate(SQLiteDatabase sb) {
        //Khởi tạo bảng;
        sb.execSQL(CREATE_TABLE_ODER_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sb, int i, int i1) {
        String dropTableOderDetail = "DROP TABLE IF EXISTS OderDetail";
        sb.execSQL(dropTableOderDetail);
        onCreate(sb);
    }
}
