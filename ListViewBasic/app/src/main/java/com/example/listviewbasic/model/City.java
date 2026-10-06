package com.example.listviewbasic.model;

public class City {
    private String NameCity;
    private int Hinh;
    private String linkWiki;

    public String getNameCity() {
        return NameCity;
    }

    public void setNameCity(String nameCity) {
        NameCity = nameCity;
    }

    public int getHinh() {
        return Hinh;
    }

    public void setHinh(int hinh) {
        Hinh = hinh;
    }

    public String getLinkWiki() {
        return linkWiki;
    }

    public void setLinkWiki(String linkWiki) {
        this.linkWiki = linkWiki;
    }

    public City(int hinh, String nameCity, String linkWiki) {
        Hinh = hinh;
        NameCity = nameCity;
        this.linkWiki = linkWiki;
    }
}
