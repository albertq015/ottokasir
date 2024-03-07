package com.ottokonek.ottokasir.utils

import android.util.Log

class LogHelper(val tag: String, val message: String?, val type: Int){

    constructor(tag: String, message: String?) : this(tag, message, error)

    companion object{
        const val error = 1
        const val debug = 2

        var isEnable = false
    }

    fun run(){
        if(isEnable)
            runLog(type)
    }

    private fun runLog(type: Int){
        when(type){
            error -> Log.e(tag, message ?: "")
            debug -> Log.d(tag, message ?: "")
        }
    }
}