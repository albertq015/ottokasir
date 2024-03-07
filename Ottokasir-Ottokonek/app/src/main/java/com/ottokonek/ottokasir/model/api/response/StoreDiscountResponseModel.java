package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import app.beelabs.com.codebase.base.response.BaseResponse;
import com.ottokonek.ottokasir.model.miscModel.LinksModel;

public class StoreDiscountResponseModel extends BaseResponse {

    public class StoreDiscDataModel {
        @JsonProperty("store_discount")
        int storeDiscount;

        public int getStoreDiscount() {
            return storeDiscount;
        }

        public void setStoreDiscount(int storeDiscount) {
            this.storeDiscount = storeDiscount;
        }
    }


    private StoreDiscDataModel data;
    private LinksModel links;

    public StoreDiscDataModel getData() {
        return data;
    }

    public void setData(StoreDiscDataModel data) {
        this.data = data;
    }

    public LinksModel getLinks() {
        return links;
    }

    public void setLinks(LinksModel links) {
        this.links = links;
    }
}

