package com.ottokonek.ottokasir.support

import android.app.Activity
import app.beelabs.com.codebase.support.util.CacheUtil
import com.ottokonek.ottokasir.IConfig.Companion.SESSION_NAME
import com.ottokonek.ottokasir.model.api.request.QrPaymentGenerateRequestModel
import com.ottokonek.ottokasir.model.api.response.KasbonCashPaymentResponseModel
import com.ottokonek.ottokasir.model.api.response.KasbonQrPaymentStatusResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentCashBondResponseModel
import com.ottokonek.ottokasir.model.api.response.PaymentCashResponseModel
import com.ottokonek.ottokasir.utils.*


class PrinterFormatter() {

    fun printCashSunMi(model: PaymentCashResponseModel.DataBean) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextSunmi("Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(model.store_name!!, 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + model.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()

        model.items?.forEach {
            AidlUtil.getInstance().printTextSunmi(it.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextSunmi("" + it.quantity + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextSunmi("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextSunmi("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Cash              " + MoneyUtil.convertIDRCurrencyFormat(model?.total_paid!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Change            " + MoneyUtil.convertIDRCurrencyFormat(model?.change!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()

    }


    fun printKasbonSunMi(model: PaymentCashBondResponseModel.DataBean) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextSunmi("Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(model.store_name!!, 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + model.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()

        model.items?.forEach {
            AidlUtil.getInstance().printTextSunmi(it.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextSunmi("" + it.quantity + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextSunmi("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextSunmi("Total Pay Later   " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        //AidlUtil.getInstance().printTextSunmi("Cash             " + MoneyUtil.convertIDRCurrencyFormat(model?.total_paid!!.toDouble()), 24f, true, false)
        //AidlUtil.getInstance().printTextSunmi("Change           " + MoneyUtil.convertIDRCurrencyFormat(model?.change!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()

    }

    fun printKasbonCashSunMi(modelPrint: KasbonCashPaymentResponseModel.DataBean) {

        var model = KasbonCashPaymentResponseModel.DataBean.OrdersBean()
        var total_amount = 0
        var total_paid = 0
        var change = 0

        for (modelOrders in modelPrint.orders!!) {
            model = modelOrders
            total_amount += modelOrders.total_amount!!.toInt()
            total_paid += modelOrders.total_paid!!.toInt()
            change += modelOrders.change!!.toInt()
        }

        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextSunmi("Repayment Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(model.store_name!!, 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.cashbond_payment_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy | HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()

        for (modelItems in modelPrint.orders!!) {

            AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(modelItems.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + modelItems.code.toString(),
                    22f,
                    false,
                    false)
            AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(modelItems.order_date,
                    "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                    22f,
                    false,
                    false)

            AidlUtil.getInstance().printLine()

            modelItems.items?.forEach {
                AidlUtil.getInstance().printTextSunmi(it.item_name, 22f, false, true)
                AidlUtil.getInstance().printTextSunmi("" + it.quantity + "x " +
                        MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                        MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
                AidlUtil.getInstance().printLine()
            }
        }

        AidlUtil.getInstance().printTextSunmi("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextSunmi("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(total_amount.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Cash              " + MoneyUtil.convertIDRCurrencyFormat(total_paid.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Change            " + MoneyUtil.convertIDRCurrencyFormat(change.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }

    fun printCashAdvan(model: PaymentCashResponseModel.DataBean) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextAdvan("Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(model.store_name!!, 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + model.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                22f,
                false,
                false)


        AidlUtil.getInstance().printLine()

        model.items?.forEach {
            AidlUtil.getInstance().printTextAdvan(it.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextAdvan("" + it.quantity + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextAdvan("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextAdvan("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Cash              " + MoneyUtil.convertIDRCurrencyFormat(model?.total_paid!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Change            " + MoneyUtil.convertIDRCurrencyFormat(model?.change!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }

    fun printKasbonAdvan(model: PaymentCashBondResponseModel.DataBean) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextAdvan("Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(model.store_name!!, 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + model.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                22f,
                false,
                false)


        AidlUtil.getInstance().printLine()

        model.items?.forEach {
            AidlUtil.getInstance().printTextAdvan(it.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextAdvan("" + it.quantity + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                    MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextAdvan("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextAdvan("Total Pay Later   " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        //AidlUtil.getInstance().printTextAdvan("Cash             " + MoneyUtil.convertIDRCurrencyFormat(model?.total_paid!!.toDouble()), 24f, true, false)
        //AidlUtil.getInstance().printTextAdvan("Change           " + MoneyUtil.convertIDRCurrencyFormat(model?.change!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()

    }


    fun printKasbonCashAdvan(modelPrint: KasbonCashPaymentResponseModel.DataBean) {

        var model = KasbonCashPaymentResponseModel.DataBean.OrdersBean()
        var total_amount = 0
        var total_paid = 0
        var change = 0

        for (modelOrders in modelPrint.orders!!) {
            model = modelOrders
            total_amount += modelOrders.total_amount!!.toInt()
            total_paid += modelOrders.total_paid!!.toInt()
            change += modelOrders.change!!.toInt()
        }

        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextAdvan("Repayment Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(model.store_name!!, 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.cashbond_payment_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy | HH:mm"),
                22f,
                false,
                false)


        AidlUtil.getInstance().printLine()


        for (modelItems in modelPrint.orders!!) {

            AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(modelItems?.order_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy") + "          " + modelItems.code.toString(),
                    22f,
                    false,
                    false)
            AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(modelItems?.order_date,
                    "dd MMM yyyy | HH:mm:ss", "HH:mm"),
                    22f,
                    false,
                    false)


            AidlUtil.getInstance().printLine()

            modelItems.items?.forEach {
                AidlUtil.getInstance().printTextAdvan(it.item_name, 22f, false, true)
                AidlUtil.getInstance().printTextAdvan("" + it.quantity + "x " +
                        MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!! / it.quantity) + "          " +
                        MoneyUtil.convertIDRCurrencyFormat(it.price?.toDouble()!!), 22f, false, false)
                AidlUtil.getInstance().printLine()
            }
        }

        AidlUtil.getInstance().printTextAdvan("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextAdvan("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(total_amount.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Cash              " + MoneyUtil.convertIDRCurrencyFormat(total_paid.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Change            " + MoneyUtil.convertIDRCurrencyFormat(change.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }


    /**
     * --------Print QR-------
     * */
    fun printQrSunMi(model: QrPaymentGenerateRequestModel, activity: Activity) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextSunmi("Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(CacheUtil.getPreferenceString(SESSION_NAME, activity), 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.date, "dd MMM yyyy | HH:mm", "dd MMM yyyy") + "          " + model?.bill_ref_num.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.date,
                "dd MMM yyyy | HH:mm", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()

        model.products?.forEach {
            AidlUtil.getInstance().printTextSunmi(it.name, 22f, false, true)
            AidlUtil.getInstance().printTextSunmi("" + it.qty + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.amount!!.toDouble() / it.qty!!) + "           " +
                    MoneyUtil.convertIDRCurrencyFormat(it.amount.toDouble()), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }

        AidlUtil.getInstance().printTextSunmi("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextSunmi("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Account           " + TextUtil.maskNumber(model?.customerName), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Application       " + model?.issuer, 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }

    fun printKasbonQrSunMi(model: KasbonQrPaymentStatusResponseModel.DataBean) {

        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextSunmi("Repayment Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(model.store_name, 24f, true, true)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.cashbond_payment_date, "dd MMM yyyy | HH:mm:ss", "dd MMM yyyy | HH:mm"),
                22f,
                false,
                false)


        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm", "dd MMM yyyy") + "          " + model?.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextSunmi(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()


        for (model in model.items!!) {
            AidlUtil.getInstance().printTextSunmi(model.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextSunmi("" + model.quantity + "x " + MoneyUtil.convertIDRCurrencyFormat(model.product_price!!.toDouble()) + "           " + MoneyUtil.convertIDRCurrencyFormat(model.price!!.toDouble()), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextSunmi("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextSunmi("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Account           " + TextUtil.maskNumber(model?.account), 24f, true, false)
        AidlUtil.getInstance().printTextSunmi("Application       " + model?.issuer, 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()

    }

    fun printQrAdvan(model: QrPaymentGenerateRequestModel, activity: Activity) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextAdvan("Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(CacheUtil.getPreferenceString(SESSION_NAME, activity), 24f, true, true)

        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.date, "dd MMM yyyy | HH:mm", "dd MMM yyyy") + "          " + model?.bill_ref_num.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.date,
                "dd MMM yyyy | HH:mm", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()

        model.products?.forEach {
            AidlUtil.getInstance().printTextAdvan(it.name, 22f, false, true)
            AidlUtil.getInstance().printTextAdvan("" + it.qty + "x " +
                    MoneyUtil.convertIDRCurrencyFormat(it.amount!!.toDouble() / it.qty!!) + "           " +
                    MoneyUtil.convertIDRCurrencyFormat(it.amount.toDouble()), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }

        AidlUtil.getInstance().printTextAdvan("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextAdvan("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Account           " + TextUtil.maskNumber(model?.customerName), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Application       " + model?.issuer, 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }

    fun printKasbonQrAdvan(model: KasbonQrPaymentStatusResponseModel.DataBean) {
        AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft())
        AidlUtil.getInstance().printTextAdvan("Repayment Pay Later Receipt", 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(model.store_name, 24f, true, true)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.cashbond_payment_date, "dd MMM yyyy | HH:mm", "dd MMM yyyy | HH:mm"),
                22f,
                false,
                false)


        AidlUtil.getInstance().printLine()

        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date, "dd MMM yyyy | HH:mm", "dd MMM yyyy") + "          " + model?.code.toString(),
                22f,
                false,
                false)
        AidlUtil.getInstance().printTextAdvan(DateUtil.simplifyDateFormat(model?.order_date,
                "dd MMM yyyy | HH:mm", "HH:mm"),
                22f,
                false,
                false)

        AidlUtil.getInstance().printLine()


        for (model in model.items!!) {
            AidlUtil.getInstance().printTextAdvan(model.item_name, 22f, false, true)
            AidlUtil.getInstance().printTextAdvan("" + model.quantity + "x " + MoneyUtil.convertIDRCurrencyFormat(model.product_price!!.toDouble()) + "           " + MoneyUtil.convertIDRCurrencyFormat(model.price!!.toDouble()), 22f, false, false)
            AidlUtil.getInstance().printLine()
        }


        AidlUtil.getInstance().printTextAdvan("---------------------------------", 22f, false, false)
        AidlUtil.getInstance().printTextAdvan("Total Payment     " + MoneyUtil.convertIDRCurrencyFormat(model?.total_amount!!.toDouble()), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Account           " + TextUtil.maskNumber(model?.account), 24f, true, false)
        AidlUtil.getInstance().printTextAdvan("Application       " + model?.issuer, 24f, true, false)
        AidlUtil.getInstance().printLine()
        AidlUtil.getInstance().print3Line()
    }

}
