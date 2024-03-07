package com.ottokonek.ottokasir

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import app.beelabs.com.codebase.base.BaseApp
import app.beelabs.com.codebase.di.component.AppComponent
import app.beelabs.com.codebase.di.component.DaggerAppComponent

class App : BaseApp() {

    companion object {
        var context: Context? = null
        fun getAppComponent(): AppComponent? {
            if (com.ottokonek.ottokasir.App.Companion.context == null) return null
            return getComponent()
        }
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        setupBuilder(DaggerAppComponent.builder(), this)
        setupDefaultFont("font/Avenir-Medium.ttf")
        com.ottokonek.ottokasir.dao.manager.LocalDbManager.setupRealm(this)
        //TODO Enable this if firebase is on
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val description = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("4CHAN", name, importance)
            channel.description = description
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }


}