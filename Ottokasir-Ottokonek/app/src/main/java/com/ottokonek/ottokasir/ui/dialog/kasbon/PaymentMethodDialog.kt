package com.ottokonek.ottokasir.ui.dialog.kasbon

import android.content.Context
import android.os.Bundle
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.dialog_payment_method_selected.*

class PaymentMethodDialog(@NonNull context: Context, style: Int, private val totalAmount: String, private val callback: PaymentMethodCallback) : BottomSheetDialog(context, style) {

    private var paymentMethod = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_payment_method_selected)
        setCancelable(false)

        contentPaymentMethod()
        actionPaymentMethod()
    }


    private fun contentPaymentMethod() {

        tvPrice.text = MoneyUtil.convertIDRCurrencyFormat(
                totalAmount.toDouble())
    }


    private fun actionPaymentMethod() {

        ivClose.setOnClickListener {
            this.dismiss()
        }

        btnCash.setOnClickListener {
            paymentMethod = "Cash"
            callback.onSelectMethod(paymentMethod)
            this.dismiss()

        }

        btnQR.setOnClickListener {
            paymentMethod = "QR"
            callback.onSelectMethod(paymentMethod)
            this.dismiss()
        }
    }
}


