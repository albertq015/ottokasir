package com.ottokonek.ottokasir.ui.dialog.customer

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageView
import androidx.annotation.NonNull
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.utils.DateUtil
import kotlinx.android.synthetic.main.dialog_filter_history_customer.*
import java.util.*

class CustomerFilterHistoryDialog(@NonNull context: Context, style: Int, private val callbackCustomerFilter: CustomerFilterCallback) : BottomSheetDialog(context, style) {

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var sheetView: View
    private val calendar = Calendar.getInstance()

    private var typeTransaction = ""
    private var startDate = ""
    private var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_filter_history_customer)
        setCancelable(false)


        actionFilterHistory()
    }


    private fun actionFilterHistory() {

        ivClose.setOnClickListener {
            this.dismiss()
        }

        tvReset.setOnClickListener {
            tvStartDate.text = ""
            tvEndDate.text = ""
            typeTransaction = ""

            tvSemua.setBackgroundResource(R.drawable.rounded_border_grey)

            tvCash.setBackgroundResource(R.drawable.rounded_border_grey)

            tvKasbon.setBackgroundResource(R.drawable.rounded_border_grey)

            tvQr.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tvSemua.setOnClickListener {
            typeTransaction = ""
            tvSemua.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tvCash.setBackgroundResource(R.drawable.rounded_border_grey)

            tvQr.setBackgroundResource(R.drawable.rounded_border_grey)

            tvKasbon.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tvCash.setOnClickListener {

            typeTransaction = "Cash"
            tvCash.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tvSemua.setBackgroundResource(R.drawable.rounded_border_grey)

            tvQr.setBackgroundResource(R.drawable.rounded_border_grey)

            tvKasbon.setBackgroundResource(R.drawable.rounded_border_grey)

        }

        tvQr.setOnClickListener {

            typeTransaction = "QR"
            tvQr.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tvSemua.setBackgroundResource(R.drawable.rounded_border_grey)

            tvCash.setBackgroundResource(R.drawable.rounded_border_grey)

            tvKasbon.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tvKasbon.setOnClickListener {

            typeTransaction = "Pay Later"
            tvKasbon.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tvSemua.setBackgroundResource(R.drawable.rounded_border_grey)

            tvCash.setBackgroundResource(R.drawable.rounded_border_grey)

            tvQr.setBackgroundResource(R.drawable.rounded_border_grey)
        }



        tvStartDate.setOnClickListener {
            if (tvStartDate.tag != null) {
                showBottomPanel(true, tvStartDate.tag as Long)
            } else {
                showBottomPanel(true, Calendar.getInstance().timeInMillis)
            }
        }

        tvEndDate.setOnClickListener {
            if (tvEndDate.tag != null) {
                showBottomPanel(false, tvEndDate.tag as Long)
            } else {
                showBottomPanel(false, Calendar.getInstance().timeInMillis)
            }
        }

        btnTerapkan.setOnClickListener {
            this.dismiss()
            callbackCustomerFilter.onFilterCustomer(typeTransaction, startDate, endDate)
        }

    }


    override fun onStart() {
        super.onStart()
        bottomSheetDialog = BottomSheetDialog(context, R.style.dialog)
        sheetView = layoutInflater.inflate(R.layout.content_panel_calendar, null)
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setCancelable(false)
    }

    private fun showBottomPanel(isStartDate: Boolean, time: Long) {
        bottomSheetDialog.show();

        val ivClose = sheetView.findViewById<ImageView>(R.id.ivClose)
        val maCalendar = sheetView.findViewById<CalendarView>(R.id.cvCalendar)
        val btnSave = sheetView.findViewById<Button>(R.id.btnSave)

        maCalendar.maxDate = Calendar.getInstance().timeInMillis
        maCalendar.date = time

        btnSave.requestFocus()

        maCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val d = dayOfMonth
            calendar.set(year, month, dayOfMonth)
        }

        ivClose.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        btnSave.setOnClickListener {
            bottomSheetDialog.dismiss()

            if (isStartDate) {
                tvStartDate.text = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "dd MMMM yyyy")
                tvStartDate.tag = calendar.timeInMillis
                startDate = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy-MM-dd")
            } else {
                tvEndDate.text = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "dd MMMM yyyy")
                tvEndDate.tag = calendar.timeInMillis

                endDate = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy-MM-dd")
            }
        }

    }

    override fun onBackPressed() {
        this.dismiss()
        super.onBackPressed()
    }
}