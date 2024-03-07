package com.ottokonek.ottokasir.ui.dialog.kasbon

interface KasbonFilterCallback {

    fun onSelectedFilter(period: Int, start: String, end: String)
}