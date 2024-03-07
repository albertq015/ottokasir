package com.ottokonek.ottokasir.model.miscModel;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.ottokonek.ottokasir.model.db.ProductitemRealmModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductItemModel implements Parcelable {

    /**
     * stock_id : 271
     * name : Cuci Bedcover
     * item_code : OKP000384
     * barcode :
     * image :
     * price : 50000
     * discount : 0
     * category : nonfood
     * status : true
     * stocks : 0
     * product_id : 384
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
    private int stock_alert;
    private boolean show_alert;
    private boolean stock_active;
    private int product_id;
    private String product_type;
    private String warehouse_name;
    private int user_id;
    private int count;

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

    public boolean isStock_active() {
        return stock_active;
    }

    public void setStock_active(boolean stock_active) {
        this.stock_active = stock_active;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static Creator<ProductItemModel> getCREATOR() {
        return CREATOR;
    }

    public ProductitemRealmModel convertToRealm() {
        ProductitemRealmModel convertedModel = new ProductitemRealmModel();
        convertedModel.setStock_id(this.stock_id);
        convertedModel.setName(this.name);
        convertedModel.setItem_code(this.item_code);
        convertedModel.setBarcode(this.barcode);
        convertedModel.setImage(this.image);
        convertedModel.setPrice(this.price);
        convertedModel.setDiscount(this.discount);
        convertedModel.setCategory(this.category);
        convertedModel.setStatus(this.status);
        convertedModel.setStocks(this.stocks);
        convertedModel.setStock_alert(this.stock_alert);
        convertedModel.setShow_alert(this.show_alert);
        convertedModel.setStock_active(this.stock_active);
        convertedModel.setProduct_id(this.product_id);
        convertedModel.setProduct_type(this.product_type);
        convertedModel.setWarehouse_name(this.warehouse_name);
        convertedModel.setUser_id(this.user_id);
        convertedModel.setCount(this.count);
        return convertedModel;
    }

    public ProductItemModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.stock_id);
        dest.writeString(this.name);
        dest.writeString(this.item_code);
        dest.writeString(this.barcode);
        dest.writeString(this.image);
        dest.writeString(this.price);
        dest.writeString(this.discount);
        dest.writeString(this.category);
        dest.writeString(String.valueOf(this.status));
        dest.writeInt(this.stocks);
        dest.writeInt(this.stock_alert);
        dest.writeString(String.valueOf(this.show_alert));
        dest.writeString(String.valueOf(this.stock_active));
        dest.writeInt(this.product_id);
        dest.writeString(this.product_type);
        dest.writeString(this.warehouse_name);
        dest.writeInt(this.user_id);
        dest.writeInt(this.count);
    }

    protected ProductItemModel(Parcel in) {
        this.stock_id = in.readInt();
        this.name = in.readString();
        this.item_code = in.readString();
        this.barcode = in.readString();
        this.image = in.readString();
        this.price = in.readString();
        this.discount = in.readString();
        this.category = in.readString();
        this.status = in.readInt() == 1;
        this.stocks = in.readInt();
        this.stock_alert = in.readInt();
        this.show_alert = in.readInt() == 1;
        this.stock_active = in.readInt() == 1;
        this.product_id = in.readInt();
        this.product_type = in.readString();
        this.warehouse_name = in.readString();
        this.user_id = in.readInt();
        this.count = in.readInt();
    }

    public static final Creator<ProductItemModel> CREATOR = new Creator<ProductItemModel>() {
        @Override
        public ProductItemModel createFromParcel(Parcel source) {
            return new ProductItemModel(source);
        }

        @Override
        public ProductItemModel[] newArray(int size) {
            return new ProductItemModel[size];
        }
    };
}