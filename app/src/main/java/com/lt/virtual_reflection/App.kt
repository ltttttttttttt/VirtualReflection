package com.lt.virtual_reflection

import android.app.Application

/**
 * creator: lt  2022/10/19  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}