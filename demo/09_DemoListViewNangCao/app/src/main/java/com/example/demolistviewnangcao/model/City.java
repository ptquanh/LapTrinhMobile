package com.example.demolistviewnangcao.model;

public class City {
    private String nameCity;
    private int hinh;
    private String linkWiki;

    public City(String nameCity, int hinh, String linkWiki) {
        this.nameCity = nameCity;
        this.hinh = hinh;
        this.linkWiki = linkWiki;
    }

    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public String getLinkWiki() {
        return linkWiki;
    }

    public void setLinkWiki(String linkWiki) {
        this.linkWiki = linkWiki;
    }
}