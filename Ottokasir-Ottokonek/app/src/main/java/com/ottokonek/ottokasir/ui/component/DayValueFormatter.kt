package com.ottokonek.ottokasir.ui.component

import android.app.Activity
import com.github.mikephil.charting.formatter.ValueFormatter
import com.ottokonek.ottokasir.utils.DateUtil

class DayValueFormatter(val activity: Activity) : ValueFormatter() {
    fun getDate(dayName: String): String {
        var hari = ""
        when (dayName) {
            "Monday" -> hari = "Monday"
            "Tuesday" -> hari = "Tuesday"
            "Wednesday" -> hari = "Wednesday"
            "Thursday" -> hari = "Thursday"
            "Friday" -> hari = "Friday"
            "Saturday" -> hari = "Saturday"
            "Sunday" -> hari = "Sunday"
            else -> {
                hari = dayName
            }
        }

        return hari
    }


    override fun getFormattedValue(value: Float): String {
        when (value) {
//            1f -> return getDate(DateUtil.getDayByRange(-6, "EEEE,dd MMM"))
//            2f -> return getDate(DateUtil.getDayByRange(-5, "EEEE,dd MMM"))
//            3f -> return getDate(DateUtil.getDayByRange(-4, "EEEE,dd MMM"))
//            4f -> return getDate(DateUtil.getDayByRange(-3, "EEEE,dd MMM"))
//            5f -> return getDate(DateUtil.getDayByRange(-2, "EEEE,dd MMM"))
//            6f -> return getDate(DateUtil.getDayByRange(-1, "EEEE,dd MMM"))
//            7f -> return getDate(DateUtil.getDayByRange(0, "EEEE,dd MMM"))

            1f -> return getDate(DateUtil.getDayByRange(-6, "EEEE"))
            2f -> return getDate(DateUtil.getDayByRange(-5, "EEEE"))
            3f -> return getDate(DateUtil.getDayByRange(-4, "EEEE"))
            4f -> return getDate(DateUtil.getDayByRange(-3, "EEEE"))
            5f -> return getDate(DateUtil.getDayByRange(-2, "EEEE"))
            6f -> return getDate(DateUtil.getDayByRange(-1, "EEEE"))
            7f -> return getDate(DateUtil.getDayByRange(0, "EEEE"))
            else -> {
                return "null"
            }
        }
    }


}