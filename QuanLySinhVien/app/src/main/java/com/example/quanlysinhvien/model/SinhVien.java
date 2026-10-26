package com.example.quanlysinhvien.model;

import java.util.Date;

public class SinhVien {
    private String id;
    private String hoten;
    private Date ngaysinh;
    private Integer lophocid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public SinhVien() {
    }

    public Date getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(Date ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public Integer getLophocid() {
        return lophocid;
    }

    public void setLophocid(Integer lophocid) {
        this.lophocid = lophocid;
    }

    public SinhVien(String id, String hoten, Date ngaysinh, Integer lophocid) {
        this.id = id;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.lophocid = lophocid;
    }
}
