package com.ottokonek.ottokasir.utils

import org.jetbrains.annotations.NotNull
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MoneyUtil {

    companion object {
        fun convertIDRCurrencyFormat(amount: Any?): String? {

            /*val numfor = NumberFormat.getNumberInstance(Locale("en", "PH")) as DecimalFormat
            val result = DecimalFormat("#.##")
            result.roundingMode = RoundingMode.UP
            return "PHP " + result.format(numfor.parse(amount.toString())).toDouble().toString()*/

            val v = amount.toString()
            var price = "PHP "
            val numberFormat = NumberFormat.getNumberInstance()
            try {
                val temp = numberFormat.format(v.toDouble())
                price += temp.replace(".", ".")
                price = price.replace("-", "")
            } catch (e: Exception) {
                e.printStackTrace()
                price += "0"
            }
            return price
        }

        fun convertCurrencyPHP1(value: Any): String? {
            val v = value.toString()
            var price = "PHP "
            val numberFormat = NumberFormat.getNumberInstance()
            try {
                val temp = numberFormat.format(v.toDouble())
                price += temp.replace(".", ".")
                price = price.replace("-", "")
            } catch (e: Exception) {
                e.printStackTrace()
                price += "0"
            }
            return price
        }

        fun convertCurrencyPHP(value: Any): String? {
            val v = value.toString()
            var price = ""
            val numberFormat = NumberFormat.getNumberInstance()
            try {
                val temp = numberFormat.format(v.toDouble())
                price += temp.replace(".", ",")
                price = price.replace("-", "")
            } catch (e: Exception) {
                e.printStackTrace()
                price += "0"
            }
            return price
        }


        fun InputDecimal(amount: String): String? {
            var inputAmount = "0"
            var result = "PHP 0"
            inputAmount = amount.replace("[^\\d.]".toRegex(), "")
            if (amount.contains(".")) {
                val separated = inputAmount.split("\\.".toRegex()).toTypedArray()
                var partDecimal = ""
                if (separated.size > 1) {
                    if (separated[1] != null && !separated[1].isEmpty()) {
                        partDecimal = if (separated[1].length > 2) {
                            separated[1].substring(0, 2)
                        } else {
                            separated[1]
                        }
                    }
                    inputAmount = separated[0]
                }
                result = convertCurrencyPHP(inputAmount).toString()
                result += ".$partDecimal"
            } else if (amount.trim { it <= ' ' } == "PHP") {
                result = ""
            } else {
                result = convertCurrencyPHP(inputAmount).toString()
            }
            return result
        }


        fun CurrencyToDouble(@NotNull amount: String?): Double {
            if (amount != null) {
                val value = amount.replace("[^\\d.,]".toRegex(), "")
                val numfor = NumberFormat.getNumberInstance(Locale("en", "PH")) as DecimalFormat
                val result = DecimalFormat("#.##")
                result.roundingMode = RoundingMode.UP
                try {
                    return result.format(numfor.parse(value)).toDouble()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            return 0.0
        }
    }

}