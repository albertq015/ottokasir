package com.ottokonek.ottokasir.model.api.response

import app.beelabs.com.codebase.base.response.BaseResponse
import app.beelabs.com.codebase.base.response.DefaultMetaResponse
import com.ottokonek.ottokasir.model.db.OrderItemRealmModel
import java.io.Serializable

class DailyOmzetResponseModel : BaseResponse() {


    var data: DataBean? = null
    var meta: DefaultMetaResponse? = null

    class DataBean {
        var transactions: List<TransactionsBean>? = null


        class TransactionsBean : Serializable {

            var type: String? = null
            var merchant_name: String? = null
            var amount: Int = 0
            var reference_number: String? = null
            var description: String? = null
            var date: Int = 0
            var status: String? = null
            var direction: String? = null
            var payment_method: String? = null

            var no_resi: String? = null
            var biller_reference: String? = null
            var stroom_token: String? = null
            var customer_id: String? = null
            var customerName: String? = null
            var commission: String? = null


            constructor(type: String?, merchant_name: String?, amount: Int, reference_number: String?, description: String?, date: Int, status: String?, direction: String?, payment_method: String?, no_resi: String?, biller_reference: String?, stroom_token: String?, customer_id: String?, customer_name: String?, commission: String?) {
                this.type = type
                this.merchant_name = merchant_name
                this.amount = amount
                this.reference_number = reference_number
                this.description = description
                this.date = date
                this.status = status
                this.direction = direction
                this.payment_method = payment_method
                this.no_resi = no_resi
                this.biller_reference = biller_reference
                this.stroom_token = stroom_token
                this.customer_id = customer_id
                this.customerName = customerName
                this.commission = commission
            }

            constructor()




            fun changeFromRealm(orderData:OrderItemRealmModel?) : TransactionsBean   {


                return TransactionsBean(orderData?.type,orderData?.merchant_name, orderData?.amount!!,
                        orderData?.reference_number,orderData?.description, orderData?.date,orderData?.status,
                        orderData?.direction,orderData?.payment_method,orderData?.no_resi,orderData?.biller_reference,
                        orderData?.stroom_token,orderData?.customer_id,orderData?.customerName,orderData?.commission)
            }

            fun changeToRealm():OrderItemRealmModel?    {
                var orderData:OrderItemRealmModel? = OrderItemRealmModel()
                orderData?.type = this.type
                orderData?.merchant_name = this.merchant_name
                orderData?.amount = this.amount
                orderData?.reference_number = this.reference_number
                orderData?.description = this.description
                orderData?.date = this.date
                orderData?.status= this.status
                orderData?.direction = this.direction
                orderData?.payment_method = this.payment_method
                orderData?.no_resi = this.no_resi
                orderData?.biller_reference = this.biller_reference
                orderData?.stroom_token = this.stroom_token
                orderData?.customer_id =this.customer_id
                orderData?.customerName = this.customerName
                orderData?.commission = this.commission

                return orderData
            }
        }
    }
}
