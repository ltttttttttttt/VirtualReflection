package com.lt.virtual_reflection

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.lt.virtual_reflection.bean.A
import com.lt.virtual_reflection.bean.B
import newInstance

//VirtualReflection
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        setContentView(tv)
        initView(tv)
    }

    private fun initView(tv: TextView) {
        tv.text = "${A::class.newInstance()}\n${B::class.newInstance(6, "VirtualReflection")}"
    }
}


