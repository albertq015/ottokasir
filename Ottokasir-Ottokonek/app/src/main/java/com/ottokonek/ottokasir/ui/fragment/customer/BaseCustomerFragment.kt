package com.ottokonek.ottokasir.ui.fragment.customer

import com.ottokonek.ottokasir.ui.activity.customer.CustomerMainActivity
import com.ottokonek.ottokasir.ui.fragment.BaseLocalFragment

open class BaseCustomerFragment : BaseLocalFragment() {

    fun getMainActivity(): CustomerMainActivity? = activity as CustomerMainActivity?

    /*fun showLoading(isShow: Boolean) = getMainActivity()?.showLoading()

    override fun getCurrentActivity(): BaseActivity = activity as BaseLocalActivity

    override fun getRootView(): View? {
        return null
    }*/
}