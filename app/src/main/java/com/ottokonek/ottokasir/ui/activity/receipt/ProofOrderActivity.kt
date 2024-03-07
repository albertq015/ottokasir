package com.ottokonek.ottokasir.ui.activity.receipt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.OrderResponseModel
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.dialog.loading.CustomProgressDialog
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_proof_order.*


class ProofOrderActivity : com.ottokonek.ottokasir.ui.activity.BaseLocalActivity() {
    private var dialog: CustomProgressDialog? = null
    private var dataModel: OrderResponseModel.DataResponseModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proof_order)

        detailAction.setOnClickListener {
            var intent = Intent(this@ProofOrderActivity, ReceiptNoteActivity::class.java)
            intent.putExtra(com.ottokonek.ottokasir.IConfig.ORDER_DETAIL_SERIALIZABLE, dataModel)
            startActivity(intent)
        }

        sendReceiptProofButton.setOnClickListener {
            val amount = orderTotalPaid.text as String
            sendReceiptNote(amount)
        }
        printReceiptButton.setOnClickListener {
            printReceiptNote()
        }

        back_appbar.setOnClickListener {
            onBackPressed()
        }

        btn_newtrans.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@ProofOrderActivity, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        })

        if (intent.hasExtra("fromdetail") && intent.getBooleanExtra("fromdetail", true)) {
            btn_newtrans.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n")
    fun setupUI(data: OrderResponseModel.DataResponseModel) {
        orderCode.setText("No. ${data.code}")
        orderDate.setText(data.orderDate)
        orderTotalPaid.text = MoneyUtil.convertIDRCurrencyFormat(data.totalAmount.toDouble())
        orderQty.text = "Jumlah Barang : ${data.totalItems} Item"
        orderPaidAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.paidNominal.toDouble())
        orderChangesAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.change.toDouble())
    }

    fun sendReceiptNote(message: String) {
        try {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Testing to WA")
            sendIntent.type = "text/plain"
            sendIntent.setPackage("com.whatsapp")
            startActivity(sendIntent)
        } catch (e: Exception) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun printReceiptNote() {
        var intent = Intent(this@ProofOrderActivity, ReceiptNoteActivity::class.java)
        intent.putExtra(com.ottokonek.ottokasir.IConfig.ORDER_DETAIL_SERIALIZABLE, dataModel)
        intent.putExtra("PRINTMENOW", true)
        startActivity(intent)

    }

    override fun onBackPressed() {
        if (intent.hasExtra("fromdetail") && intent.getBooleanExtra("fromdetail", true))
            finish()
        else {
            var intent = Intent(this@ProofOrderActivity, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}

