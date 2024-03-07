package com.ottokonek.ottokasir.ui.activity.receipt

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.dao.manager.BluetoothManager
import com.ottokonek.ottokasir.model.api.response.OrderResponseModel
import com.ottokonek.ottokasir.model.pojo.ItemModel
import com.ottokonek.ottokasir.ui.activity.product_list.ProductListActivity
import com.ottokonek.ottokasir.ui.adapter.ReceiptOrderItemAdapter
import com.ottokonek.ottokasir.utils.MoneyUtil
import kotlinx.android.synthetic.main.activity_receipt_note.*
import kotlinx.android.synthetic.main.content_order_item.*


class ReceiptNoteActivity : com.ottokonek.ottokasir.ui.activity.BaseLocalActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receipt_note)

        orderItemList.setHasFixedSize(true)
        orderItemList.layoutManager = LinearLayoutManager(this)


        var dataModel = intent.getSerializableExtra(com.ottokonek.ottokasir.IConfig.ORDER_DETAIL_SERIALIZABLE) as OrderResponseModel.DataResponseModel
        setupUI(dataModel)

        sendReceiptButton.setOnClickListener {
            val amount = detailTotalPaid.text as String
            sendReceiptNote(amount)
        }


        backAction.setOnClickListener {
            super.onBackPressed()
        }

        btn_newtrans.setOnClickListener(View.OnClickListener {
            var intent = Intent(this@ReceiptNoteActivity, ProductListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        })

        if (intent.hasExtra("fromdetail") && intent.getBooleanExtra("fromdetail", true)) {
            btn_newtrans.visibility = View.GONE
        }
    }

    fun setupUI(data: OrderResponseModel.DataResponseModel) {
        detailCodeNo.text = "No. ${data.code}"
        detailStoreName.text = data.storeName
        detailOrderDate.text = data.orderDate
        detailTotalPaid.text = MoneyUtil.convertIDRCurrencyFormat(data.totalAmount.toDouble())
        detailPaidAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.paidNominal.toDouble())

        val storeDiscount: Double = data.storeDiscount.toDouble()
        detailStoreDiscount.text = MoneyUtil.convertIDRCurrencyFormat(storeDiscount)


        val totalPrice: Double = getTotalPriceFromItems(data.items!!)
        if (totalPrice > data.totalPaid.toDouble()) detailPriceBeforeDiscount.text = MoneyUtil.convertIDRCurrencyFormat(totalPrice)
        else detailPriceBeforeDiscount.text = ""
        detailPriceBeforeDiscount.paintFlags = detailPriceBeforeDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        detailChangesAmount.text = MoneyUtil.convertIDRCurrencyFormat(data.change.toDouble())
        detailTotalQty.text = "${data.totalItems} Item"


        val items: List<ItemModel> = data.items as List<ItemModel>
        val adapter = ReceiptOrderItemAdapter(items, this@ReceiptNoteActivity)
        orderItemList.adapter = adapter
        if (intent.hasExtra("PRINTMENOW") && intent.getBooleanExtra("PRINTMENOW", false)) {
            printReceiptNote(data)
        }

        printReceiptButton.setOnClickListener {
            printReceiptNote(data)
        }
    }

    private fun getTotalPriceFromItems(items: List<ItemModel>): Double {
        var totalPrice = 0.0
        for (item: ItemModel in items) {
            totalPrice += item.price.toDouble()
        }

        return totalPrice
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


    fun printReceiptNote(data: OrderResponseModel.DataResponseModel) {
        val btman = BluetoothManager()

        if (btman.btAdapter == null) {
            showToast("Bluetooth tidak tedeteksi, mohon Aktifkan blietooth anda")
            return
        }
        btman.listener = object : BluetoothManager.BluetoothManagerListener {
            override fun onNoBluetooth() {
                showToast("Mohon Aktifkan blietooth anda")
            }

            override fun onNoDevice() {
                showToast("Koneksi bluetooth tidak tedeksi, mohon aktifkan bluetooth anda dan sambungkan ke printer")
            }

            override fun onDeviceSelected(bluetoothDevice: BluetoothDevice) {
                btman.setPrinter(bluetoothDevice)
            }

            override fun onConnected() {
                val items: List<ItemModel> = data.items as List<ItemModel>
                btman.printer.printText("\n")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_CENTER)
                btman.printer.printImage(BitmapFactory.decodeResource(resources, R.drawable.logo_print_2))
                btman.printer.printText("\n")
                btman.printer.printText(data.storeName)
                btman.printer.printText("\n")
                btman.printer.printText(com.ottokonek.ottokasir.presenter.manager.SessionManager.getAddress(this@ReceiptNoteActivity))
                btman.printer.printText("\n")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("No. " + data.code)
                btman.printer.printText("\n")
                btman.printer.printText(data.orderDate)
                btman.printer.printText("\n")
                btman.printer.printLine()
                btman.printer.printText("\n")

                items.forEach {
                    btman.printer.printText("\n")

                    btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                    btman.printer.printText(it.item_name)
                    btman.printer.printText("\n")


                    btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                    btman.printer.printText("X " + it.quantity)
                    btman.printer.printText("\n")

                    btman.printer.printText(MoneyUtil.convertIDRCurrencyFormat(it.price.toDouble()))
                    btman.printer.printText("\n")
                }

                btman.printer.printLine()
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("Total")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                btman.printer.printText("" + detailTotalQty.text)
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("Total Bayar")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                btman.printer.printText("" + detailTotalPaid.text)
                btman.printer.printText("\n")
                btman.printer.printLine()
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("Jumlah Dibayar")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                btman.printer.printText("" + detailPaidAmount.text)
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                btman.printer.printText("(" + detailTotalPaid.text + ")")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("Kembali")
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_RIGHT)
                btman.printer.printText("" + detailChangesAmount.text)
                btman.printer.printText("\n")
                btman.printer.setAlign(com.ottokonek.ottokasir.utils.BluetoothUtil.ALIGN_LEFT)
                btman.printer.printText("\n")
                btman.printer.printText("\n")
                btman.printer.printText("\n")
            }

            override fun onFailed() {
                onApiFailed("", "Printing Gagal")
            }

        }
        btman.showPrinterSelectorDialog(this)
    }

}