package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.base.response.DefaultMetaResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionReportResponse extends BaseResponse {

    /**
     * data : {"totalAmount":210000,"jenisTransaksi":[{"name":"Cash","qty":8,"amount":210000}],"jenisProduk":[{"name":"Freshcare Citrus Roll On","qty":1,"amount":15000,"sub_amount":15000},{"name":"Voucher pulsa 100.000","qty":2,"amount":90000,"sub_amount":180000}]}
     * links : null
     * meta : {"status":true,"code":200,"message":"OK"}
     */

    private DataBean data;
    private Object links;
    private DefaultMetaResponse meta;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getLinks() {
        return links;
    }

    public void setLinks(Object links) {
        this.links = links;
    }

    public DefaultMetaResponse getMeta() {
        return meta;
    }

    public void setMeta(DefaultMetaResponse meta) {
        this.meta = meta;
    }

    public static class DataBean {
        /**
         * totalAmount : 210000
         * jenisTransaksi : [{"name":"Cash","qty":8,"amount":210000}]
         * jenisProduk : [{"name":"Freshcare Citrus Roll On","qty":1,"amount":15000,"sub_amount":15000},{"name":"Voucher pulsa 100.000","qty":2,"amount":90000,"sub_amount":180000}]
         */

        private Double totalAmount;
        private List<JenisTransaksiBean> jenisTransaksi;
        private List<JenisProdukBean> jenisProduk;

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<JenisTransaksiBean> getJenisTransaksi() {
            return jenisTransaksi;
        }

        public void setJenisTransaksi(List<JenisTransaksiBean> jenisTransaksi) {
            this.jenisTransaksi = jenisTransaksi;
        }

        public List<JenisProdukBean> getJenisProduk() {
            return jenisProduk;
        }

        public void setJenisProduk(List<JenisProdukBean> jenisProduk) {
            this.jenisProduk = jenisProduk;
        }

        public static class JenisTransaksiBean {
            /**
             * name : Cash
             * qty : 8
             * amount : 210000
             */

            private String name;
            private int qty;
            private Double amount;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getQty() {
                return qty;
            }

            public void setQty(int qty) {
                this.qty = qty;
            }

            public Double getAmount() {
                return amount;
            }

            public void setAmount(Double amount) {
                this.amount = amount;
            }
        }

        public static class JenisProdukBean {
            /**
             * name : Freshcare Citrus Roll On
             * qty : 1
             * amount : 15000
             * sub_amount : 15000
             */

            private String name;
            private int qty;
            private Double amount;
            private Double sub_amount;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getQty() {
                return qty;
            }

            public void setQty(int qty) {
                this.qty = qty;
            }

            public Double getAmount() {
                return amount;
            }

            public void setAmount(Double amount) {
                this.amount = amount;
            }

            public Double getSub_amount() {
                return sub_amount;
            }

            public void setSub_amount(Double sub_amount) {
                this.sub_amount = sub_amount;
            }
        }
    }
}
