package com.lt.virtual_reflection

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.lt.virtual_reflection.bean.A
import com.lt.virtual_reflection.bean.B
import com.lt.virtual_reflection.bean_c.C
import com.lt.virtual_reflection.bean_d.D
import com.lt.virtual_reflection.bean_e.E1
import com.lt.virtual_reflection.bean_e.E2
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
        tv.text = listOf(
            A::class.newInstance(),
            B::class.newInstance(6, "VirtualReflection"),
            C::class.newInstance(),
            D::class.newInstance(),
            E1::class.newInstance(),
            E2::class.newInstance("test"),
        ).joinToString("\n")
    }
}


