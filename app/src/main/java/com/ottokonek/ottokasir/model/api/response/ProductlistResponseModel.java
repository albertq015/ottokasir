package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;
import com.ottokonek.ottokasir.model.miscModel.LinksModel;
import com.ottokonek.ottokasir.model.miscModel.ProductItemModel;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductlistResponseModel extends BaseResponse {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class productListDataResponseModel {

        private List<ProductItemModel> products;

        public List<ProductItemModel> getProducts() {
            return products;
        }

        public void setProducts(List<ProductItemModel> products) {
            this.products = products;
        }
    }

    private productListDataResponseModel data;
    private LinksModel links;

    public productListDataResponseModel getData() {
        return data;
    }

    public void setData(productListDataResponseModel data) {
        this.data = data;
    }

    public LinksModel getLinks() {
        return links;
    }

    public void setLinks(LinksModel links) {
        this.links = links;
    }


}



