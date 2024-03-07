package com.ottokonek.ottokasir.dao.transaction

import com.ottokonek.ottokasir.dao.manager.LocalDbManager
import com.ottokonek.ottokasir.model.api.request.PaymentCashBondRequestModel
import com.ottokonek.ottokasir.model.api.request.PaymentCashRequestModel
import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.model.db.OrderItemRealmModel
import com.ottokonek.ottokasir.model.db.ProductitemRealmModel
import io.realm.Realm
import io.realm.RealmResults
import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import com.ottokonek.ottokasir.model.db.TransactionModel
import com.ottokonek.ottokasir.model.miscModel.ItemModel
import io.realm.Case

object TransactionManager {

    val orderData: RealmResults<*>?
        get() {
            val r = Realm.getDefaultInstance()
            return if (r.where(OrderItemRealmModel::class.java) != null)
                r.where(OrderItemRealmModel::class.java).findAll()
            else
                null
        }

    val transactionData: RealmResults<*>?
        get() {
            val r = Realm.getDefaultInstance()
            return if (r.where(TransactionModel::class.java) != null)
                r.where(TransactionModel::class.java).findAll()
            else
                null
        }

    fun getSearchData(value: String): RealmResults<OrderItemRealmModel>? {
        var realm = Realm.getDefaultInstance()
        return if (realm.where(OrderItemRealmModel::class.java) != null)
            realm.where(OrderItemRealmModel::class.java).equalTo("reference_number", value).findAll()
        else
            null
    }

    fun updateTransaction(data: DailyOmzetResponseModel.DataBean?) {
        data?.transactions?.forEach {
            var realmData = it.changeToRealm()
            LocalDbManager.querryRealm { realm -> realm.copyToRealm<OrderItemRealmModel>(realmData) }
        }
    }

    fun putTransaction(data: DailyOmzetResponseModel.DataBean?) {
        LocalDbManager.querryRealm { realm -> realm.delete(OrderItemRealmModel::class.java) }
        data?.transactions?.forEach {
            var realmData = it.changeToRealm()
            LocalDbManager.querryRealm { realm -> realm.copyToRealm<OrderItemRealmModel>(realmData) }
        }
    }

    fun putTransaction(data: TransactionHistoryResponse) {
        LocalDbManager.querryRealm { realm ->
            realm.delete(TransactionModel::class.java)
        }

        data?.data?.forEach {
            var realmData = it.changeToRealmObject()
            LocalDbManager.querryRealm { realm -> realm.copyToRealm<TransactionModel>(realmData) }
        }
    }

    fun delTransaction() {
        LocalDbManager.querryRealm { realm ->
            realm.delete(TransactionModel::class.java)
        }
    }

    fun getProductInOrder(): RealmResults<ProductitemRealmModel>? {
        val r = Realm.getDefaultInstance();
        val results = r.where(ProductitemRealmModel::class.java).findAll()
        if (results.size != 0)
//            return r.where(ProductitemRealmModel.class).equalTo("id", id).findFirst().getCount();
            return results
        else return null;
    }

    fun getProductsInOrderForRequest(): ArrayList<ItemModel> {
        val result = getProductInOrder()
        val product = ArrayList<ItemModel>()
        if (result != null) {
            result.forEach {
                product.add(ItemModel(it.stock_id, it.price.toDouble() * it.count, it.name, it.count))
            }
        }
        return product
    }

    fun createOrderCash(paidNominal: String, totalAmount: String, paymentType: String, customerId: Int): PaymentCashRequestModel {
        var model = PaymentCashRequestModel()
        model = PaymentCashRequestModel()
        model.payment_type = paymentType
        model.total_amount = totalAmount
        model.total_paid = paidNominal
        model.change = ((paidNominal.toDouble() - model.total_amount.toString().toDouble()).toString())
        model.customer_id = customerId

        if (getProductInOrder()?.size != 0) {
            val listOrder = ArrayList<PaymentCashRequestModel.ItemsBean>()
            getProductInOrder()!!.forEach {
                listOrder.add(PaymentCashRequestModel.ItemsBean(it.count, it.stock_id.toString()))
            }
            model.items = listOrder
        }

        return model
    }

    fun createOrderCashBond(totalAmount: String, paymentType: String, customerId: Int): PaymentCashBondRequestModel {
        var model = PaymentCashBondRequestModel()
        model = PaymentCashBondRequestModel()
        model.payment_type = paymentType
        model.total_amount = totalAmount
        model.customer_id = customerId


        if (getProductInOrder()?.size != 0) {
            val listOrder = ArrayList<PaymentCashBondRequestModel.ItemsBean>()
            getProductInOrder()!!.forEach {
                listOrder.add(PaymentCashBondRequestModel.ItemsBean(it.count, it.stock_id.toString()))
            }
            model.items = listOrder
        }

        return model
    }

    fun getTransactionBySearch(query: String): RealmResults<TransactionModel>? {
        val r = Realm.getDefaultInstance()
        return if (r.where(TransactionModel::class.java).contains("orderID", query, Case.INSENSITIVE).findAll() != null) {
            r.where(TransactionModel::class.java).contains("orderID", query, Case.INSENSITIVE).findAll()!!
        } else {
            null
        }
    }

    fun getTotalPrice(): Double {
        var price = 0.0
        var item = PaymentCashRequestModel.ItemsBean()
        getProductInOrder()!!.forEach {
            //            item = InvoicesAttributesBean(it.count,it.itemCode,it.warehouseCode)
            price = price + (it.count.times(it.price.toDouble()))
        }

        return price
    }

}
