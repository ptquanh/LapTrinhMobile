package com.example.luyentap.models;

import java.io.Serializable;
import java.util.Locale;

public class Book implements Serializable {
    private int id;
    private String title;
    private String author;
    private String genre;
    private double price;
    private int imageResId;
    private boolean inCart;
    private boolean isSelected;

    public Book() {
    }

    public Book(int id, String title, String author, String genre, double price, int imageResId, boolean inCart, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.price = price;
        this.imageResId = imageResId;
        this.inCart = inCart;
        this.isSelected = isSelected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFormattedPrice() {
        return String.format(Locale.US, "$%.2f", price);
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
