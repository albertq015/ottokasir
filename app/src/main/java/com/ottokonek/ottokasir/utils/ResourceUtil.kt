package com.ottokonek.ottokasir.utils

import com.ottokonek.ottokasir.App


class ResourceUtil {

    companion object {
        fun stringResource(resId: Int): String {
            return App.context!!.resources.getString(resId)
        }
    }
}