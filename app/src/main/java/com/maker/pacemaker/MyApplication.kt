package com.maker.pacemaker

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}