package com.example.gridview.model;

public class HinhAnh {
    private int hinh;
    private String ten;

    public HinhAnh(int hinh, String ten) {
        this.hinh = hinh;
        this.ten = ten;
    }

    public HinhAnh(String ten, int hinh) {
        this.ten = ten;
        this.hinh = hinh;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}