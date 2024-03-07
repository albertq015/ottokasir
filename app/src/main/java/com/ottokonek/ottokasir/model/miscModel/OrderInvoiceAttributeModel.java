package com.ottokonek.ottokasir.model.miscModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderInvoiceAttributeModel {
    @JsonProperty("item_code")
    private String itemCode;
    @JsonProperty("warehouse_code")
    private String warehouseCode;
    private int quantity;

    public OrderInvoiceAttributeModel() {
    }

    public OrderInvoiceAttributeModel(String itemCode, String warehouseCode, int quantity) {
        this.itemCode = itemCode;
        this.warehouseCode = warehouseCode;
        this.quantity = quantity;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getWarehouseCode() {
        return warehouseCode;
    }

    public void setWarehouseCode(String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
