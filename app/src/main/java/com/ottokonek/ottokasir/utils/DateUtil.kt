package com.ottokonek.ottokasir.utils

import android.content.Context
import android.view.View
import com.ottokonek.ottokasir.IConfig
import com.ottokonek.ottokasir.IConfig.Companion.monthLabels
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    private val TAG = DateUtil::class.java.simpleName

    fun convertFromEpochToString(epoch: Long, formatDate: String?): String {
        val tz = TimeZone.getTimeZone("GMT+08:00")
        val cal = Calendar.getInstance(tz)
        val sdf = SimpleDateFormat(formatDate)
        sdf.calendar = cal
        cal.time = convertToDate(epoch)
        val date = cal.time
        val format = sdf.format(date)
        var dateString = ""
        for (month in monthLabels) {
            val labels = month.split("_".toRegex()).toTypedArray()
            dateString = format.replace(labels[0].toRegex(), labels[0])
            if (dateString != format) break
        }
        return dateString
    }

    fun convertFromEpochToString2(epoch: Long, formatDate: String?): String {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        val sdf = SimpleDateFormat(formatDate)
        sdf.calendar = cal
        cal.time = convertToDate(epoch)
        val date = cal.time
        val format = sdf.format(date)
        var dateString = ""
        for (month in monthLabels) {
            val labels = month.split("_".toRegex()).toTypedArray()
            dateString = format.replace(labels[0].toRegex(), labels[1])
            if (dateString != format) break
        }
        return dateString
    }

    fun convertToDate(epoch: Long): Date {
        return Date(epoch * 1000L)
    }

    fun convertToCalendar(epoch: Long): Calendar {
        val date = Date(epoch * 1000L)
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0 // set hour to midnight
        cal[Calendar.MINUTE] = 0 // set minute in hour
        cal[Calendar.SECOND] = 0 // set second in minute
        cal[Calendar.MILLISECOND] = 0
        return cal
    }

    fun convertToEpoch(date: Date?): Long {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("GMT+08:00")
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0 // set hour to midnight
        cal[Calendar.MINUTE] = 0 // set minute in hour
        cal[Calendar.SECOND] = 0 // set second in minute
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis / 1000 + 86400
    }

    fun convertToEpoch3(date: Date?): Long {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("GMT+08:00")
        cal.time = date
        return cal.timeInMillis / 1000
    }

    fun convertToEpoch2(date: Date?): Long {
        val cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("GMT+08:00")
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        return cal.timeInMillis / 1000
    }

    fun convertToEpoch4(date: Date?): Long {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal.timeInMillis / 1000
    }

    fun timePicker(finalDay: String?, date: Date?, element: View?, context: Context?) {
        // Get Current Time
        val c = Calendar.getInstance()
        val mHour = intArrayOf(c[Calendar.HOUR_OF_DAY])
        val mMinute = intArrayOf(c[Calendar.MINUTE])
        val today = isToday(date)
        val map: Map<String, Int> = HashMap()
    }

    fun isToday(dt: Date?): Boolean {
        val c = Calendar.getInstance()
        val date = Calendar.getInstance()
        date.time = dt
        return c[Calendar.DATE] == date[Calendar.DATE]
    }

    fun isSameDay(previousDate: Date?, currentDate: Date?): Boolean {
        val prevCal = Calendar.getInstance()
        val currCal = Calendar.getInstance()
        prevCal.time = previousDate
        currCal.time = currentDate
        return prevCal[Calendar.DATE] == currCal[Calendar.DATE]
    }

    fun getDayByRange(rangeOfDay: Int, format: String?): String {
        val currCal = Calendar.getInstance()
        currCal.add(Calendar.DATE, rangeOfDay)
        val formatDay = SimpleDateFormat(format)
        return formatDay.format(currCal.time)
    }

    fun convertToDate(currentDate: String?, format: String?): Date? {
        val inputFormatTime = SimpleDateFormat(format)
        var date: Date? = null
        try {
            date = inputFormatTime.parse(currentDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    val todayName: String
        get() {
            val c = Calendar.getInstance()
            val formatDay = SimpleDateFormat("EEEE")
            return formatDay.format(c.time)
        }

    fun getNowTime(format: String?): String {
        val c = Calendar.getInstance()
        val formatDateOut = SimpleDateFormat(format)
        return formatDateOut.format(c.time)
    }

    fun timeChecker(selectedDay: Calendar, dayOfMonth: Int): Boolean {
        var valid = false
        val c = Calendar.getInstance()
        val formatDateOut = SimpleDateFormat("EEEE")
        val date = formatDateOut.format(selectedDay.time)
        if (date == "Sunday") {
            valid = true
        } else if (date == "Saturday") {
            if (dayOfMonth == c[Calendar.DAY_OF_MONTH] && c[Calendar.HOUR_OF_DAY] > 10) {
                valid = true
            }
        } else {
            if (dayOfMonth == c[Calendar.DAY_OF_MONTH] && c[Calendar.HOUR_OF_DAY] > 14) {
                valid = true
            }
        }
        return valid
    }

    fun comparingDate(startDate: String?, endDate: String?): Boolean {
        val date1 = convertToDate(startDate, "yyyy-MM-dd")
        val date2 = convertToDate(endDate, "yyyy-MM-dd")
        return !date2!!.before(date1)
    }

    fun convertFullDateFormat(date1: String?): String? {
        var timeDisplay: String? = null
        val inputTime = "yyyy-MM-dd"
        val outputTime = "dd MMMM yyyy"
        val inputFormatTime = SimpleDateFormat(inputTime)
        val outputFormatTime = SimpleDateFormat(outputTime)
        var date: Date? = null
        if (date1 != null) {
            try {
                date = inputFormatTime.parse(date1)
                timeDisplay = outputFormatTime.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return timeDisplay
    }

    fun simplifyDateFormat(date1: String?, inputTime: String?, outputTime: String?): String {
        val inputFormat = SimpleDateFormat(inputTime, Locale.US) //yyyy-MM-dd HH:mm:ss //yyyy-MM-dd HH:mm:ss
        val outputFormat = SimpleDateFormat(outputTime, Locale.US)

        val date: Date?
        var str: String? = null

        try {
            date = inputFormat.parse(date1)

            date?.let { str = outputFormat.format(it) }
        } catch (e: ParseException) {
            LogHelper(TAG, e.message).run()
        }

        return str ?: ""
    }

    fun getTimestamp(): String {
        return java.lang.Long.toString(System.currentTimeMillis() / 1000)
    }
}