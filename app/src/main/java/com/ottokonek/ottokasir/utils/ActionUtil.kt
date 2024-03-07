package com.ottokonek.ottokasir.utils

import android.app.Activity
import android.content.Intent
import com.ottokonek.ottokasir.presenter.OrderPresenter
import com.ottokonek.ottokasir.ui.activity.auth.LoginActivity

class ActionUtil {

    companion object {
        fun logoutAction(activity: Activity) {
            OrderPresenter.clearTransactionRealm()
            com.ottokonek.ottokasir.presenter.manager.SessionManager.clearSessionLogin(activity)
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            activity.startActivity(intent)
            activity.finish()
        }
    }


}