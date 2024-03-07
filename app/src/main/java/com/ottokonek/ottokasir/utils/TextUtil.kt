package com.ottokonek.ottokasir.utils

import android.text.Editable
import android.text.TextWatcher


open class TextUtil : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    companion object {
        fun maskPhoneNumber(phoneNumber: String): String? {
            var newNumber: StringBuilder = java.lang.StringBuilder(phoneNumber)
            if (phoneNumber != "" && phoneNumber.length == 7) {
                for (i in 0..4) {
                    newNumber.setCharAt(4 + i, '*')
                }
            }

            return newNumber.toString()
        }

        fun maskNumber(number: String?) : String {
            if(number!!.isNotEmpty()){
                val firstPart = number?.substring(0, 3)

                val len = number?.length
                val lastPart = number?.substring(number.length-4, number.length)


                return firstPart + "xxxxx" + lastPart
            }

           return ""
        }
    }

}