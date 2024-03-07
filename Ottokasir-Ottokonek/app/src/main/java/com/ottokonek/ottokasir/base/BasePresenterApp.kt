package com.ottokonek.ottokasir.base

import app.beelabs.com.codebase.base.BasePresenter
import app.beelabs.com.codebase.base.contract.IDaoPresenter
import app.beelabs.com.codebase.base.contract.IView
import com.ottokonek.ottokasir.App.Companion.context

open class BasePresenterApp(val iv: IView) : BasePresenter(), IDaoPresenter {

    private val messageGlobal: String = context.let {
        try {
            "Something went wrong"
        } catch (e: Exception) {
            "Something went wrong"
        }
    }

    override fun getPresenter(): BasePresenter = this
    /*override fun getContext(): Context = iv.currentActivity*/
}