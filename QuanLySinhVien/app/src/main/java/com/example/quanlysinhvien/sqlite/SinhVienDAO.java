package com.example.quanlysinhvien.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.quanlysinhvien.helper.DateTimeHelper;
import com.example.quanlysinhvien.helper.DbHelper;
import com.example.quanlysinhvien.model.SinhVien;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class SinhVienDAO {
    private final SQLiteDatabase db;

    public SinhVienDAO(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        this.db = dbHelper.getWritableDatabase();
    }

    public long insert(SinhVien sinhVien) {
        ContentValues values = new ContentValues();
        values.put("id", sinhVien.getId());
        values.put("hoten", sinhVien.getHoten());
        if (sinhVien.getNgaysinh() != null) {
            values.put("ngaysinh", DateTimeHelper.toString(sinhVien.getNgaysinh()));
        }
        values.put("lophocid", sinhVien.getLophocid());
        return db.insert("sinhviens", null, values);
    }

    public long update(SinhVien sinhVien) {
        ContentValues values = new ContentValues();
        values.put("hoten", sinhVien.getHoten());
        if (sinhVien.getNgaysinh() != null) {
            values.put("ngaysinh", DateTimeHelper.toString(sinhVien.getNgaysinh()));
        }
        values.put("lophocid", sinhVien.getLophocid());
        return db.update("sinhviens", values, "id=?", new String[]{sinhVien.getId()});
    }

    public List<SinhVien> get(String sql, String... selectArgs) {
        List<SinhVien> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, selectArgs);
        while (cursor.moveToNext()) {
            SinhVien sinhVien = new SinhVien();
            sinhVien.setId(cursor.getString(0));
            sinhVien.setHoten(cursor.getString(1));
            try {
                String strNgaySinh = cursor.getString(2);
                if (strNgaySinh != null) {
                    sinhVien.setNgaysinh(DateTimeHelper.toDate(strNgaySinh));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!cursor.isNull(3)) {
                sinhVien.setLophocid(cursor.getInt(3));
            }
            list.add(sinhVien);
        }
        cursor.close();
        return list;
    }

    public List<SinhVien> getAll() {
        String sql = "SELECT * FROM sinhviens";
        return get(sql);
    }

    public SinhVien getById(String id) {
        String sql = "SELECT * FROM sinhviens WHERE id=?";
        List<SinhVien> list = get(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<SinhVien> getByLopHocId(int lophocid) {
        String sql = "SELECT * FROM sinhviens WHERE lophocid=?";
        return get(sql, String.valueOf(lophocid));
    }

    public long delete(String ID) {
        return db.delete("sinhviens", "id=?", new String[]{ID});
    }
}
