package com.ottokonek.ottokasir.model.pojo;

public class InventoryModel {

    private int id_barang;
    private String nama_barang;
    private String harga_barang;
    private String stok;
    private int total_stok;

    public InventoryModel (int id_barang, String nama_barang, String harga_barang, String stok, int total_stok){
        this.id_barang = id_barang;
        this.nama_barang = nama_barang;
        this.harga_barang = harga_barang;
        this.stok = stok;
        this.total_stok = total_stok;
    }

    public int getId(){
        return id_barang;
    }

    public String getNama_barang(){
        return nama_barang;
    }

    public String getHarga_barang(){
        return harga_barang;
    }

    public String getStok(){
        return stok;
    }

    public int getTotal_stok(){
        return total_stok;
    }
}
