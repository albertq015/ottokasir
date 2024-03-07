package com.ottokonek.ottokasir.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.R
import kotlinx.android.synthetic.main.activity_productlist.*
import kotlinx.android.synthetic.main.fragment_report.*
import java.util.*
import com.ottokonek.ottokasir.utils.DateUtil
import kotlinx.android.synthetic.main.fragment_report.view.*
import com.ottokonek.ottokasir.ui.activity.history.TransactionReportActivity

class ReportFragment : BaseLocalFragment() {

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var sheetView: View
    private lateinit var mView: View
    private val calendar = Calendar.getInstance()


    private var startDate = ""
    private var endDate = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_report, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
    }


    override fun handleError(s: String) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show()
        hideLoading()
    }

    fun initContent() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)
        scanBarcode.visibility = View.GONE

        tvStartDate.text = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "dd MMMM yyyy")
        startDate = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy-MM-dd")


        tvEndDate.text = DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "dd MMMM yyyy")
        endDate =  DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy-MM-dd")

        title.visibility = View.VISIBLE
        title.setText(R.string.laporan_penjualan)
        search.visibility = View.GONE

        mView.tvStartDate.setOnClickListener {
            if(tvStartDate.tag!=null){
                showBottomPanel(true,tvStartDate.tag as Long)
            }else{
                showBottomPanel(true,Calendar.getInstance().timeInMillis)
            }
        }

        mView.tvEndDate.setOnClickListener {
            if(tvEndDate.tag!=null){
                showBottomPanel(false,tvEndDate.tag as Long)
            }else{
                showBottomPanel(false,Calendar.getInstance().timeInMillis)
            }
        }

        btnTampilkan.setOnClickListener {
            if (DateUtil.comparingDate(startDate, endDate)) {
                val intent = Intent(requireActivity(), TransactionReportActivity::class.java)
                intent.putExtra("startDate", startDate)
                intent.putExtra("endDate", endDate)
                intent.putExtra("start",tvStartDate.text.toString())
                intent.putExtra("end",tvEndDate.text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(requireActivity(), getString(R.string.tanggal_akhir_tidak_boleh_kurang_dari_tanggal_awal), Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun getCurrentActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }

    override fun onStart() {
        super.onStart()
        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.dialog)
        sheetView = getLayoutInflater().inflate(R.layout.content_panel_calendar, null);
        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.setCancelable(false)
    }

    fun showBottomPanel(isStartDate: Boolean,time: Long) {
        bottomSheetDialog.show();
        bottomSheetDialog.setOnDismissListener {
            requireActivity().background.visibility = View.GONE
        }

        requireActivity().background.visibility = View.VISIBLE
        val ivClose = sheetView!!.findViewById<ImageView>(R.id.ivClose)
        val maCalendar = sheetView!!.findViewById<CalendarView>(R.id.cvCalendar)
        val btnSave = sheetView!!.findViewById<Button>(R.id.btnSave)

        maCalendar.maxDate = Calendar.getInstance().timeInMillis
        maCalendar.date = time

        btnSave.requestFocus()

        maCalendar.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            override fun onSelectedDayChange(view: CalendarView, year: Int, month: Int, dayOfMonth: Int) {
                val d = dayOfMonth
                calendar.set(year, month, dayOfMonth)
            }
        })

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

                endDate =  DateUtil.convertFromEpochToString(DateUtil.convertToEpoch2(calendar.time), "yyyy-MM-dd")
            }
        }

    }


}