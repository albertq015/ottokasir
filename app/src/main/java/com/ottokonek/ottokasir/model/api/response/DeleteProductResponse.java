package com.ottokonek.ottokasir.model.api.response;

import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.base.response.DefaultMetaResponse;

public class DeleteProductResponse extends BaseResponse {


    /**
     * data : {}
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
    }
}
