package com.example.quanlysinhvien.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_Name = "QLSV";
    private static final int DB_Version = 1;

    public DbHelper(@Nullable Context context) {
        super(context, DB_Name, null, DB_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String lopHocSQL = "CREATE TABLE lophocs(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + " tenlop text not null)";
        String sinhVienSQL = "CREATE TABLE sinhviens(id text PRIMARY KEY, " + " hoten text not null, ngaysinh text, lophocid INTEGER, " + " FOREIGN KEY (lophocid) REFERENCES lophocs(id))";

        db.execSQL(lopHocSQL);
        db.execSQL(sinhVienSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String lopHocSQL = "DROP TABLE IF EXISTS lophocs";
        String sinhVienSQL = "DROP TABLE IF EXISTS sinhviens";

        db.execSQL(sinhVienSQL);
        db.execSQL(lopHocSQL);

        onCreate(db);
    }
}
