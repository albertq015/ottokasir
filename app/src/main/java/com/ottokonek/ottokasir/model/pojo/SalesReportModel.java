package com.ottokonek.ottokasir.model.pojo;

public class SalesReportModel {
    private int id;
    private String title;
    private String barang;
    private String date;
    private String price;
    private int total;
    private String detil;

    public SalesReportModel(int id, String title, String barang, String date, String price, int total, String detil) {
        this.id = id;
        this.title = title;
        this.barang = barang;
        this.date = date;
        this.price = price;
        this.total = total;
        this.detil = detil;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBarang() {
        return barang;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public int getTotal() {
        return total;
    }

    public String getDetil() {
        return detil;
    }
}

