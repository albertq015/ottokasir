package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.ottokonek.ottokasir.model.pojo.InvoiceModel
import com.ottokonek.ottokasir.model.pojo.ItemModel
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class OrderResponseModel : BaseResponse() {

    var data: DataResponseModel? = null
    var links: com.ottokonek.ottokasir.model.miscModel.LinksModel? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataResponseModel : Serializable {

        var id: Int = 0
        var code: String = ""

        @JsonProperty("status")
        var statusOrder: String? = ""
        @JsonProperty("store_name")
        var storeName: String? = ""
        @JsonProperty("total_paid")
        var totalPaid: String = ""
        @JsonProperty("total_amount")
        var totalAmount: String = ""
        @JsonProperty("store_discount")
        var storeDiscount: String = ""
        @JsonProperty("paid_nominal")
        var paidNominal: String = ""
        var change: String = ""
        @JsonProperty("total_items")
        var totalItems: Int = 0
        @JsonProperty("order_date")
        var orderDate: String? = ""
        var invoices: List<InvoiceModel>? = null
        var items: List<ItemModel>? = null


    }

}