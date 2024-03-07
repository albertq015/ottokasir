package com.ottokonek.ottokasir.model.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import app.beelabs.com.codebase.base.response.BaseResponse;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MasterProductResponseModel extends BaseResponse {

    /**
     * data : [{"id":1,"name":"Detol Hand Sanitizer","active":true,"barcode":"112233445566"},{"id":2,"name":"Pulsa XL 100ribu","active":true,"barcode":""}]
     * links : {}
     * meta : {"status":true,"code":200,"message":"OK"}
     */

    private LinksBean links;
    private MetaBean meta;
    private List<DataBean> data;

    public LinksBean getLinks() {
        return links;
    }

    public void setLinks(LinksBean links) {
        this.links = links;
    }

    public MetaBean getMeta() {
        return meta;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class LinksBean {
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

    public static class DataBean {
        /**
         * id : 1
         * name : Detol Hand Sanitizer
         * active : true
         * barcode : 112233445566
         */

        private int id;
        private String name;
        private boolean active;
        private String barcode;
        private String image;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
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
    }
}
