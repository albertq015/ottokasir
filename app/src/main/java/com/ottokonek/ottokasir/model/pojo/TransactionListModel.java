package com.ottokonek.ottokasir.model.pojo;

public class TransactionListModel {
    private int id_transaksi;
    private String no_trans;
    private String pembelian;
    private String jam;
    private String itm;
    private String hrg;
    private String detil_transaksi;

    public TransactionListModel(int id_transaksi, String no_trans, String pembelian, String jam, String itm, String hrg, String detil_transaksi) {
        this.id_transaksi = id_transaksi;
        this.no_trans = no_trans;
        this.pembelian = pembelian;
        this.jam = jam;
        this.itm = itm;
        this.hrg = hrg;
        this.detil_transaksi = detil_transaksi;
    }

    public int getId() {
        return id_transaksi;
    }

    public String getTrans() {
        return no_trans;
    }

    public String getPembelian() {
        return pembelian;
    }

    public String getJam() {
        return jam;
    }

    public String getItm() {
        return itm;
    }

    public String getHrg() {
        return hrg;
    }

    public String getDetil_transaksi() {
        return detil_transaksi;
    }
}
