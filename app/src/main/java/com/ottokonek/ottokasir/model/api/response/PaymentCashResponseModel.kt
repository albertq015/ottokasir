package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class PaymentCashResponseModel : BaseResponse() {

    var data: DataBean? = null
    var links: LinksBean? = null
    var meta: DefaultMetaResponse? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean : Serializable {

        var id: Int = 0
        var code: String? = null
        var status: String? = null
        var store_name: String? = null
        var merchant: String? = null
        var customer_name: String? = null
        var customer_phone: String? = null
        var total_paid: String? = null
        var payment_method: String? = null
        var total_amount: String? = null
        var store_discount: String? = null
        var paid_nominal: String? = null
        var change: String? = null
        var total_items: Int = 0
        var order_date: String? = null
        var status_cashbond: String? = null
        var active_cashbond: String? = null
        var customer_id: Int = 0
        var invoices: List<InvoicesBean>? = null
        var items: List<ItemsBean>? = null

        @JsonIgnoreProperties(ignoreUnknown = true)
        class InvoicesBean : Serializable {

            var id = 0
            var customer_code: String? = null
            var invoice_code: String? = null
            var status: String? = null
            var warehouse_name: String? = null
            var order_id = 0

        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        class ItemsBean : Serializable {

            var id = 0
            var discount: String? = null
            var price: String? = null
            var product_price: String? = null
            var quantity = 0
            var invoice_id = 0
            var item_name: String? = null
            var item_code: String? = null
            var product_type: String? = null
            var product_id = 0

        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class LinksBean : Serializable

}