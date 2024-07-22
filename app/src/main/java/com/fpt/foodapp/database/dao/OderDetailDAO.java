package com.fpt.foodapp.database.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fpt.foodapp.database.datahelper.DatabaseHelper;
import com.fpt.foodapp.dto.OderDetail;

import java.util.ArrayList;
import java.util.List;

public class OderDetailDAO {


    private SQLiteDatabase sb;

    public OderDetailDAO(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        sb = dbHelper.getWritableDatabase();
    }


    //add  Cart;
    public void insertCart(OderDetail oderDetail) {
        ContentValues values = new ContentValues();
        values.put("Image", oderDetail.getImage());
        values.put("ProductId", oderDetail.getProductId());
        values.put("ProductName", oderDetail.getProductName());
        values.put("QuanTity", oderDetail.getQuanTity());
        values.put("Price", oderDetail.getPrice());
        values.put("Discount", oderDetail.getDiscount());
        //
        sb.insert("OderDetail", null, values);

    }

    //delete sạch Cart;
    public void deleteCart() {
        String sql = "DELETE FROM OderDetail";
        sb.execSQL(sql);
    }


    //delete theo Id sản phẩm;
    public void deleteId(String id) {
        sb.delete("OderDetail", "ProductId =?", new String[]{id});
    }


    //get Cart;
    @SuppressLint("Range")
    public List<OderDetail> getCart(String sql, String... selectionArgs) {
        List<OderDetail> detailList = new ArrayList<>();
        Cursor cursor = sb.rawQuery(sql, selectionArgs);
        while (cursor.moveToNext()) {
            OderDetail oderDetail = new OderDetail();
            oderDetail.setImage(cursor.getString(cursor.getColumnIndex("Image")));
            oderDetail.setProductId(cursor.getString(cursor.getColumnIndex("ProductId")));
            oderDetail.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
            oderDetail.setQuanTity(cursor.getString(cursor.getColumnIndex("QuanTity")));
            oderDetail.setPrice(cursor.getString(cursor.getColumnIndex("Price")));
            oderDetail.setDiscount(cursor.getString(cursor.getColumnIndex("Discount")));
            //
            detailList.add(oderDetail);
        }
        return detailList;

    }


    //get All;
    public List<OderDetail> getAll() {
        String sql = "SELECT * FROM OderDetail";
        return getCart(sql);
    }


}
