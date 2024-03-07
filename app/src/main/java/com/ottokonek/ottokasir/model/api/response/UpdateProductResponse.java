package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProductResponse extends BaseResponse {

    /**
     * data : {"stock_id":846,"name":"Susu bearband","item_code":"OKP000540","barcode":"21042020111","image":"","price":"6500","discount":"0","category":"food","status":true,"stocks":1,"product_id":540,"product_type":"Product","warehouse_name":"Toko Vina","user_id":25}
     * links : null
     * meta : {"status":true,"code":200,"message":"OK"}
     */

    private DataBean data;
    private Object links;
    private MetaBean meta;

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

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DataBean {
        /**
         * stock_id : 846
         * name : Susu bearband
         * item_code : OKP000540
         * barcode : 21042020111
         * image :
         * price : 6500
         * discount : 0
         * category : food
         * status : true
         * stocks : 1
         * product_id : 540
         * product_type : Product
         * warehouse_name : Toko Vina
         * user_id : 25
         */

        private int stock_id;
        private String name;
        private String item_code;
        private String barcode;
        private String image;
        private String price;
        private String discount;
        private String category;
        private boolean status;
        private int stocks;
        private int product_id;
        private int stock_alert;
        private boolean show_alert;
        private boolean is_stock_active;
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

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getStocks() {
            return stocks;
        }

        public void setStocks(int stocks) {
            this.stocks = stocks;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
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

    public static class MetaBean {
        /**
         * status : true
         * code : 200
         * message : OK
         */

        private boolean status;
        private int code;
        private String message;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
