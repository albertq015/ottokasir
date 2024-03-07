package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.beelabs.com.codebase.base.response.BaseResponse;
import app.beelabs.com.codebase.base.response.DefaultMetaResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckRefundResponseModel extends BaseResponse {

    /**
     * data : {"rc":"00","refundable":true,"refundRrn":"301343254802"}
     * meta : {"status":true,"code":200,"message":"Succesful"}
     */

    private DataBean data;
    private DefaultMetaResponse meta;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public DefaultMetaResponse getMeta() {
        return meta;
    }

    public void setMeta(DefaultMetaResponse meta) {
        this.meta = meta;
    }

    public static class DataBean {
        /**
         * rc : 00
         * refundable : true
         * refundRrn : 301343254802
         */

        private String rc;
        private boolean refundable;
        private String refundRrn;

        public String getRc() {
            return rc;
        }

        public void setRc(String rc) {
            this.rc = rc;
        }

        public boolean isRefundable() {
            return refundable;
        }

        public void setRefundable(boolean refundable) {
            this.refundable = refundable;
        }

        public String getRefundRrn() {
            return refundRrn;
        }

        public void setRefundRrn(String refundRrn) {
            this.refundRrn = refundRrn;
        }
    }
}
