package com.ottokonek.ottokasir.model.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreDiscountRequestModel {

    @JsonProperty("item_codes")
    List<Integer> itemCodes;

    public List<Integer> getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(List<Integer> itemCodes) {
        this.itemCodes = itemCodes;
    }
}
