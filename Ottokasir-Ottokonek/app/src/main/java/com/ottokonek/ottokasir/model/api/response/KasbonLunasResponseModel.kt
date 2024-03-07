package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ottokonek.ottokasir.model.pojo.kasbon.OrderBean
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class KasbonLunasResponseModel : BaseResponse() {

    var links: Any? = null
//    var meta: MetaBean? = null
    var data: List<DataBean>? = null

//    class MetaBean {
//        var isStatus = false
//        var code = 0
//        var message: String? = null
//
//    }

    class DataBean : Serializable {
        var order_date: String? = null
        var total_cashbond: String? = null
        var order: List<OrderBean>? = null

//        class OrderBean : Serializable {
//            var id = 0
//            var code: String? = null
//            var status: String? = null
//            var store_name: String? = null
//            var merchant: String? = null
//            var customer_name: String? = null
//            var customer_phone: String? = null
//            var total_paid: String? = null
//            var payment_method: String? = null
//            var total_amount: String? = null
//            var store_discount: String? = null
//            var paid_nominal: String? = null
//            var change: String? = null
//            var total_items = 0
//            var order_date: String? = null
//            var status_cashbond: String? = null
//            var active_cashbond: String? = null
//            var customer_id = 0
//            var invoices: Any? = null
//            var details: Any? = null
//            var items: List<ItemsBean>? = null
//
//            class ItemsBean : Serializable {
//                var id = 0
//                var discount: String? = null
//                var price: String? = null
//                var product_price: String? = null
//                var quantity = 0
//                var invoice_id = 0
//                var item_name: String? = null
//                var item_code: String? = null
//                var image: String? = null
//                var product_type: String? = null
//                var product_id = 0
//
//            }
//        }
    }
}