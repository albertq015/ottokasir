package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.base.response.DefaultMetaResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateProductResponse extends BaseResponse {


    /**
     * data : {"stock_id":1321,"name":"Lucky Strike Red","item_code":"OKP000661","barcode":"25042036","image":"product-image-805187196.","price":"25000","discount":"0","category":"food","status":true,"stocks":100,"stock_alert":10,"show_alert":true,"product_id":661,"product_type":"Product","warehouse_name":"Test UAT QRIS Dua","user_id":41}
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean {
        /**
         * stock_id : 1321
         * name : Lucky Strike Red
         * item_code : OKP000661
         * barcode : 25042036
         * image : product-image-805187196.
         * price : 25000
         * discount : 0
         * category : food
         * status : true
         * stocks : 100
         * stock_alert : 10
         * show_alert : true
         * product_id : 661
         * product_type : Product
         * warehouse_name : Test UAT QRIS Dua
         * user_id : 41
         */

        private int stock_id;
        private String name;
        private String item_code;
        private String barcode;
        private String image;
        private String price;
        private String discount;
        private String category;
        @SerializedName("status")
        private boolean statusX;
        private int stocks;
        private int stock_alert;
        private boolean show_alert;
        private boolean is_stock_active;
        private int product_id;
        private String product_type;
        private String warehouse_name;
        private int user_id;

        public int getStock_id() {
            return stock_id;
        }

        public void setStock_id(int stock_id) {
            this.stock_id = stock_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItem_code() {
            return item_code;
        }

        public void setItem_code(String item_code) {
            this.item_code = item_code;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public boolean isStatusX() {
            return statusX;
        }

        public void setStatusX(boolean statusX) {
            this.statusX = statusX;
        }

        public int getStocks() {
            return stocks;
        }

        public void setStocks(int stocks) {
            this.stocks = stocks;
        }

        public int getStock_alert() {
            return stock_alert;
        }

        public void setStock_alert(int stock_alert) {
            this.stock_alert = stock_alert;
        }

        public boolean isShow_alert() {
            return show_alert;
        }

        public void setShow_alert(boolean show_alert) {
            this.show_alert = show_alert;
        }

        public boolean isIs_stock_active() {
            return is_stock_active;
        }

        public void setIs_stock_active(boolean is_stock_active) {
            this.is_stock_active = is_stock_active;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getProduct_type() {
            return product_type;
        }

        public void setProduct_type(String product_type) {
            this.product_type = product_type;
        }

        public String getWarehouse_name() {
            return warehouse_name;
        }

        public void setWarehouse_name(String warehouse_name) {
            this.warehouse_name = warehouse_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }
    }
}
