package com.ottokonek.ottokasir.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import app.beelabs.com.codebase.base.BaseActivity
import com.ottokonek.ottokasir.IConfig
import app.beelabs.com.codebase.base.BasePresenter
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.gson.Gson

import com.ottokonek.ottokasir.R
import com.ottokonek.ottokasir.model.api.response.DailyOmzetResponseModel
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.ui.adapter.TransactionAdapter
import com.ottokonek.ottokasir.ui.component.DayValueFormatter
import com.ottokonek.ottokasir.ui.component.XYMarkerView
import kotlinx.android.synthetic.main.fragment_transaction.*
import java.util.ArrayList
import com.ottokonek.ottokasir.ui.component.MyValueFormatter
import com.ottokonek.ottokasir.ui.activity.history.TransactionHistoryActivity
import com.ottokonek.ottokasir.ui.activity.history.TransactionIView
import io.realm.RealmResults
import com.ottokonek.ottokasir.dao.transaction.TransactionManager
import com.ottokonek.ottokasir.model.api.request.HistoryRequestModel
import com.ottokonek.ottokasir.model.api.response.TransactionHistoryResponse
import com.ottokonek.ottokasir.model.db.TransactionModel
import com.ottokonek.ottokasir.model.miscModel.ItemModel
import com.ottokonek.ottokasir.model.pojo.ChartModel
import com.ottokonek.ottokasir.presenter.manager.SessionManager
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity
import com.ottokonek.ottokasir.utils.DateUtil

class TransactionFragment : BaseLocalFragment(), OnChartValueSelectedListener, TransactionIView {


    val presenter: OrderPresenter = BasePresenter.getInstance(this, OrderPresenter::class.java) as OrderPresenter
    private lateinit var leftAxis: YAxis
    lateinit var mView: View
    lateinit var adapter: TransactionAdapter
    var arrayData = arrayOf(
            ChartModel(0.0, 0),
            ChartModel(0.0, 0),
            ChartModel(0.0, 0),
            ChartModel(0.0, 0),
            ChartModel(0.0, 0),
            ChartModel(0.0, 0),
            ChartModel(0.0, 0))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_transaction, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initContent()
        setBarChart()
        callAPI()
    }

    override fun handleSuccess(data: TransactionHistoryResponse) {
        //hideLoading()

        saveToRealm(data)
        val transactionHistory: RealmResults<TransactionModel> = TransactionManager.transactionData as RealmResults<TransactionModel>

        if (transactionHistory.size < 3) {
            adapter.setData(transactionHistory, transactionHistory.size)

        } else {
            adapter.setData(transactionHistory, 3)
        }

        addDataToChart(data)
        setDataByValue()

        /*if (data.data?.size != 0) {
            saveToRealm(data)

            rvTransaction.layoutManager
            val transactionHistory: RealmResults<TransactionModel> = TransactionManager.transactionData as RealmResults<TransactionModel>

            if (transactionHistory.size < 3) {
                adapter.setData(transactionHistory, transactionHistory.size)

            } else {
                adapter.setData(transactionHistory, 3)
            }
            if (data.data != null) {
                addDataToChart(data)
            }
            setDataByValue()
            hideLoading()
        }*/
    }

    override fun onPause() {
        super.onPause()
        presenter.onDestroy()
    }

    override fun handleError(s: String) {
        Toast.makeText(requireActivity(), s, Toast.LENGTH_LONG).show()
    }

    override fun saveToRealm(data: TransactionHistoryResponse) {
        presenter.saveToRealm(data)
    }


    override fun getCurrentActivity(): BaseActivity {
        return requireActivity() as BaseActivity
    }

    override fun onNothingSelected() {
        println()
    }

    override fun handleSuccessLoadMore(data: DailyOmzetResponseModel) {
    }

    override fun sessionExpired() {
        Toast.makeText(context, R.string.info_logout, Toast.LENGTH_LONG).show()
        SessionManager.clearSessionLogin(context)
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        println()
    }

    fun initContent() {
        val title = activity!!.findViewById<TextView>(R.id.title)
        title.visibility = View.VISIBLE
        title.setText(R.string.transaksi)
        val search = activity!!.findViewById<ConstraintLayout>(R.id.view_search)
        search.visibility = View.GONE

        val scanBarcode = activity!!.findViewById<ImageView>(R.id.iv_scan)
        scanBarcode.visibility = View.GONE

        rvTransaction.setHasFixedSize(true)
        rvTransaction.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = TransactionAdapter(requireContext())
        rvTransaction.adapter = adapter

        tvMoveToList.setOnClickListener {
            val intent = Intent(requireActivity(), TransactionHistoryActivity::class.java)
            intent.putExtra(IConfig.Companion.IS_FROM_OMZET, false)
            startActivity(intent)
        }

        tvShowChart.setOnClickListener {
            showHideChart()
        }

        ivShowChart.setOnClickListener {
            showHideChart()
        }

        tvValue.setOnClickListener {
            tvValue.setBackgroundResource(R.drawable.rounded_fill_blue)
            tvQuantity.setBackgroundResource(0)
            tvValue.setTextColor(resources.getColor(R.color.white))
            tvQuantity.setTextColor(resources.getColor(R.color.black))
            setDataByValue()
        }

        tvQuantity.setOnClickListener {
            tvQuantity.setBackgroundResource(R.drawable.rounded_fill_blue)
            tvValue.setBackgroundResource(0)
            tvValue.setTextColor(resources.getColor(R.color.black))
            tvQuantity.setTextColor(resources.getColor(R.color.white))
            setByQuantity()
        }

    }

    fun setBarChart() {

        bcTransaction.setDrawBarShadow(false)
        bcTransaction.setDrawValueAboveBar(true)
        bcTransaction.getDescription().setEnabled(false)
        bcTransaction.animateY(2000)
        bcTransaction.setOnChartValueSelectedListener(null)
        bcTransaction.setMaxVisibleValueCount(60)
        bcTransaction.isDoubleTapToZoomEnabled = false
        bcTransaction.setDrawMarkers(false)
        val xAxisFormatter: ValueFormatter = DayValueFormatter(requireActivity())
        val xAxis = bcTransaction.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.labelCount = 7
        xAxis.valueFormatter = xAxisFormatter

        leftAxis = bcTransaction.getAxisLeft()
        leftAxis.setLabelCount(5, false)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f)

        val rightAxis = bcTransaction.axisRight
        rightAxis.setLabelCount(0, true)
        rightAxis.setDrawTopYLabelEntry(false)
        rightAxis.setDrawZeroLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.textColor = activity!!.resources.getColor(R.color.white)
        rightAxis.axisMinimum = 0f

        val l = bcTransaction.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setForm(Legend.LegendForm.SQUARE)
        l.setFormSize(12f)
        l.setTextSize(12f)
        l.setXEntrySpace(5f)

        val xyMarkerView = XYMarkerView(requireContext(), xAxisFormatter)
        xyMarkerView.chartView = bcTransaction
        bcTransaction.marker = xyMarkerView

    }

    fun addDataToChart(data: TransactionHistoryResponse) {
        var listOfDate = ArrayList<String>()
        listOfDate.add(DateUtil.getDayByRange(-6, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(-5, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(-4, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(-3, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(-2, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(-1, "dd-MM-yyyy"))
        listOfDate.add(DateUtil.getDayByRange(0, "dd-MM-yyyy"))

        for (i in 0..data.data!!.size - 1) {

            var orderDate = DateUtil.simplifyDateFormat(data.data?.get(i)?.transactionDate
                    , "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy")
            var orderData = data.data?.get(i)
            if (orderDate.equals(listOfDate.get(0))) {
                pushToArrayData(6, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(1))) {
                pushToArrayData(5, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(2))) {
                pushToArrayData(4, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(3))) {
                pushToArrayData(3, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(4))) {
                pushToArrayData(2, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(5))) {
                pushToArrayData(1, orderData!!.amount, getQuantityFromTransaction(orderData))
            } else if (orderDate.equals(listOfDate.get(6))) {
                pushToArrayData(0, orderData!!.amount, getQuantityFromTransaction(orderData))
            }
        }
    }

    fun pushToArrayData(index: Int, value: Double, quantity: Int) {
        arrayData.set(index, ChartModel(arrayData.get(index).price + value, arrayData.get(index).quantity + quantity))
    }

    fun showHideChart() {
        if (bcTransaction.visibility == View.GONE) {
            bcTransaction.visibility = View.VISIBLE
            lySwitch.visibility = View.VISIBLE
            tvShowChart.setText(resources.getString(R.string.sembunyikan_grafik_penjualan))
            ivShowChart.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_white_24dp)
        } else {
            bcTransaction.visibility = View.GONE
            tvShowChart.setText(resources.getString(R.string.tunjukan_grafik_penjualan))
            lySwitch.visibility = View.GONE
            ivShowChart.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_white_24dp)
        }
    }

    private fun callAPI() {
        showLoading()
        val request = HistoryRequestModel()
        request.date_start = ""
        request.date_end = ""
        presenter.getTransactionHistory(requireActivity(), request)
    }

    fun setDataByValue() {
        if (bcTransaction.data != null) {
            bcTransaction.clear()
        }

        leftAxis.setValueFormatter(MyValueFormatter("value"))

        val values = ArrayList<BarEntry>()

        values.add(BarEntry(1f, arrayData.get(6).price.toFloat()))
        values.add(BarEntry(2f, arrayData.get(5).price.toFloat()))
        values.add(BarEntry(3f, arrayData.get(4).price.toFloat()))
        values.add(BarEntry(4f, arrayData.get(3).price.toFloat()))
        values.add(BarEntry(5f, arrayData.get(2).price.toFloat()))
        values.add(BarEntry(6f, arrayData.get(1).price.toFloat()))
        values.add(BarEntry(7f, arrayData.get(0).price.toFloat()))
        val barDataSet = BarDataSet(values, resources.getString(R.string.transaksi_penjualan_seminggu_terakhir))

        barDataSet.setColor(resources.getColor(R.color.colorPrimaryDark))

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.9f

        bcTransaction.setData(data)
    }

    fun setByQuantity() {
        if (bcTransaction.data != null) {
            bcTransaction.clear()
        }

        leftAxis.setValueFormatter(MyValueFormatter("quantity"))

        val values = ArrayList<BarEntry>()
        values.add(BarEntry(1f, arrayData.get(6).quantity.toFloat()))
        values.add(BarEntry(2f, arrayData.get(5).quantity.toFloat()))
        values.add(BarEntry(3f, arrayData.get(4).quantity.toFloat()))
        values.add(BarEntry(4f, arrayData.get(3).quantity.toFloat()))
        values.add(BarEntry(5f, arrayData.get(2).quantity.toFloat()))
        values.add(BarEntry(6f, arrayData.get(1).quantity.toFloat()))
        values.add(BarEntry(7f, arrayData.get(0).quantity.toFloat()))
        val barDataSet = BarDataSet(values, resources.getString(R.string.transaksi_penjualan_seminggu_terakhir))

        barDataSet.setColor(resources.getColor(R.color.colorPrimaryDark))
        barDataSet.valueFormatter = MyValueFormatter("quantity")
        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(barDataSet)

        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.9f

        bcTransaction.setData(data)
    }

//    fun setByQuantity() {
//        if (bcTransaction.data != null) {
//            bcTransaction.clear()
//        }
//        leftAxis.setValueFormatter(MyValueFormatter("quantity"))
//
//        val values = ArrayList<BarEntry>()
////        val bar = BarEntry(1f, arrayData.get(6).quantity.toFloat())
////        bar.
//        values.add(BarEntry(1f, arrayData.get(6).quantity.toFloat()))
//        values.add(BarEntry(2f, arrayData.get(5).quantity.toFloat()))
//        values.add(BarEntry(3f, arrayData.get(4).quantity.toFloat()))
//        values.add(BarEntry(4f, arrayData.get(3).quantity.toFloat()))
//        values.add(BarEntry(5f, arrayData.get(2).quantity.toFloat()))
//        values.add(BarEntry(6f, arrayData.get(1).quantity.toFloat()))
//        values.add(BarEntry(7f, arrayData.get(0).quantity.toFloat()))
//
//        val barDataSet = BarDataSet(values, "Transaksi penjualan seminggu terakhir")
//
//        barDataSet.setColor(resources.getColor(R.color.navy_blue))
//
//        val dataSets = ArrayList<IBarDataSet>()
//        dataSets.add(barDataSet)
//
//        val data = BarData(dataSets)
//        data.setValueTextSize(10f)
//        data.barWidth = 0.9f
//
//        bcTransaction.setData(data)
//    }

    fun getQuantityFromTransaction(data: TransactionHistoryResponse.DataBean): Int {
        val gson = Gson()
        var quantity = 0
        val items = gson.fromJson(data.products, Array<ItemModel>::class.java)
        for (item in items) {
            quantity = quantity.plus(item.qty!!.toInt())
        }

        return quantity
    }
}


