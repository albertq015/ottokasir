package com.ottokonek.ottokasir.model.api.request;

import java.util.List;

public class StoreTypeProductAddRequestModel {

    /**
     * store_type_id : 1
     * master_product_ids : [5]
     */

    private int store_type_id;
    private List<Integer> master_product_ids;

    public int getStore_type_id() {
        return store_type_id;
    }

    public void setStore_type_id(int store_type_id) {
        this.store_type_id = store_type_id;
    }

    public List<Integer> getMaster_product_ids() {
        return master_product_ids;
    }

    public void setMaster_product_ids(List<Integer> master_product_ids) {
        this.master_product_ids = master_product_ids;
    }
}
