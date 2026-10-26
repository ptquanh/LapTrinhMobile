package com.example.quanlysinhvien.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.helper.DbHelper;
import com.example.quanlysinhvien.model.LopHoc;

import java.util.ArrayList;
import java.util.List;

public class LopHocDAO {
    private SQLiteDatabase db;
    public LopHocDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }
    
    public long insert(LopHoc lopHoc){
        ContentValues values = new ContentValues();
        values.put("tenlop", lopHoc.getTenlophoc());
        return db.insert("lophocs", null, values);
    }

    public List<LopHoc> get(String sql, String ... selectArgs){
        List<LopHoc> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);
        while (cursor.moveToNext()) {
            LopHoc lopHoc = new LopHoc();
            lopHoc.setId(cursor.getInt(0));
            lopHoc.setTenlophoc(cursor.getString(1));
            list.add(lopHoc);
        }
        return list;
    }

    public List<LopHoc> getAll(){
        String sql = "SELECT * FROM lophocs";
        return get(sql);
    }

    public long delete(int ID){
        return db.delete("lophocs", "id=?", new String[] {String.valueOf(ID)});
    }
}
