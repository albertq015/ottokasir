package com.ottokonek.ottokasir.ui.dialog.kasbon

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
import kotlinx.android.synthetic.main.dialog_filter_kasbon.*
import java.util.*

class KasbonFilterDialog(@NonNull context: Context, style: Int, private val callbackFilter: KasbonFilterCallback) : BottomSheetDialog(context, style) {

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var sheetView: View
    private val calendar = Calendar.getInstance()

    private var typePeriod = 0
    private var startDate = ""
    private var endDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_filter_kasbon)
        setCancelable(false)

        actionFilterKasbon()
    }


    private fun actionFilterKasbon() {
        ivClose.setOnClickListener {
            this.dismiss()
        }

        tvReset.setOnClickListener {
            tvStartDate.text = ""
            tvEndDate.text = ""
            typePeriod = 0

            tv3Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv7Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv14Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv30Days.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tv3Days.setOnClickListener {

            typePeriod = 3
            tv3Days.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tv7Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv14Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv30Days.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tv7Days.setOnClickListener {

            typePeriod = 7
            tv7Days.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tv3Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv14Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv30Days.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tv14Days.setOnClickListener {

            typePeriod = 14
            tv14Days.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tv3Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv7Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv30Days.setBackgroundResource(R.drawable.rounded_border_grey)
        }

        tv30Days.setOnClickListener {

            typePeriod = 30
            tv30Days.setBackgroundResource(R.drawable.rounded_fill_blue_ocean)

            tv3Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv7Days.setBackgroundResource(R.drawable.rounded_border_grey)

            tv14Days.setBackgroundResource(R.drawable.rounded_border_grey)
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
            callbackFilter.onSelectedFilter(typePeriod, startDate, endDate)
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