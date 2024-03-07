package com.ottokonek.ottokasir.model.api.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse

@JsonIgnoreProperties(ignoreUnknown = true)
class CashModelResponse : BaseResponse() {


    /**
     * data : {"id":7,"code":"201906241014040001","status":"Order Created","store_name":"warung oke","total_paid":"200000.0","payment_method":"PM0003","total_amount":"125000.0","store_discount":"0.0","paid_nominal":"125000.0","change":"75000.0","total_items":2,"order_date":"24 Jun 2019 | 10:14:04","invoices":[{"id":7,"customer_code":"P0003","invoice_code":null,"status":"created","warehouse_name":"warung oke","warehouse_code":"W0033","order_id":7}],"items":[{"id":12,"discount":"0.0","price":"50000.0","quantity":1,"invoice_id":7,"item_name":"PAKET 1 EKONOMIS","item_code":"I0005"},{"id":13,"discount":"0.0","price":"75000.0","quantity":1,"invoice_id":7,"item_name":"PAKET 2 EKONOMIS","item_code":"I0007"}]}
     * links : {}
     * meta : {"status":true,"code":200,"message":"OK"}
     */

    var data: DataBean? = null
    var links: LinksBean? = null
    var meta: DefaultMetaResponse? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean {
        /**
         * id : 7
         * code : 201906241014040001
         * status : Order Created
         * store_name : warung oke
         * total_paid : 200000.0
         * payment_method : PM0003
         * total_amount : 125000.0
         * store_discount : 0.0
         * paid_nominal : 125000.0
         * change : 75000.0
         * total_items : 2
         * order_date : 24 Jun 2019 | 10:14:04
         * invoices : [{"id":7,"customer_code":"P0003","invoice_code":null,"status":"created","warehouse_name":"warung oke","warehouse_code":"W0033","order_id":7}]
         * items : [{"id":12,"discount":"0.0","price":"50000.0","quantity":1,"invoice_id":7,"item_name":"PAKET 1 EKONOMIS","item_code":"I0005"},{"id":13,"discount":"0.0","price":"75000.0","quantity":1,"invoice_id":7,"item_name":"PAKET 2 EKONOMIS","item_code":"I0007"}]
         */

        var id: Int = 0
        var code: String? = null
        var status: String? = null
        var store_name: String? = null
        var total_paid: String? = null
        var payment_method: String? = null
        var total_amount: String? = null
        var store_discount: String? = null
        var paid_nominal: String? = null
        private val order_date: String? = null
        private val invoicesBean: List<InvoicesBean>? = null
        private val items: List<ItemsBean>? = null

        @JsonIgnoreProperties(ignoreUnknown = true)
        class InvoicesBean {
            /**
             * id : 7
             * customer_code : P0003
             * invoice_code : null
             * status : created
             * warehouse_name : warung oke
             * warehouse_code : W0033
             * order_id : 7
             */

            var id: Int = 0
            var customer_code: String? = null
            var invoice_code: Any? = null
            var status: String? = null
            var warehouse_name: String? = null
            var warehouse_code: String? = null
            var order_id: Int = 0
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class ItemsBean
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class LinksBean
}
