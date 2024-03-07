package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.ottokonek.ottokasir.model.db.TransactionModel
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class TransactionHistoryResponse : BaseResponse() {

    //    var meta: DefaultMetaResponse? = null
    var data: List<DataBean>? = null

    @JsonIgnoreProperties(ignoreUnknown = true)
    class DataBean : Serializable {
        /**
         * id : 1
         * orderID : 1111111
         * paymentType : Cash
         * amount : 100000
         * referenceNumber : 123124142122
         * customerPhone :
         * transactionDate : 2019-06-26T15:20:10Z
         * status : Success
         * products :
         */

        var id: Int = 0
        var orderID: String? = null
        var paymentType: String? = null
        var amount = 0.0
        var totalPaid = 0.0
        var change = 0.0
        var referenceNumber: String? = null
        var customerPhone: String? = null
        var transactionDate: String? = null
        var customerName: String = ""
        var issuer: String = ""
        var status: String? = null
        var transactionDirection: String? = null
        var products: String? = null
        var refundStatus: String = ""
        var refundRrn: String = ""

        fun changeToRealmObject(): TransactionModel {
            var realmObject = TransactionModel()
            realmObject.amount = this.amount
            realmObject.customerPhone = this.customerPhone
            realmObject.id = this.id
            realmObject.orderID = this.orderID
            realmObject.paymentType = this.paymentType
            realmObject.products = this.products
            realmObject.referenceNumber = this.referenceNumber
            realmObject.status = this.status
            realmObject.transactionDirection = this.transactionDirection
            realmObject.change = this.change
            realmObject.transactionDate = this.transactionDate
            realmObject.customerName = this.customerName
            realmObject.issuer = this.issuer
            realmObject.totalPaid = this.totalPaid
            realmObject.refundStatus = this.refundStatus
            realmObject.refundRrn = this.refundRrn

            return realmObject
        }
    }

}
