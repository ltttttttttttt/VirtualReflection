package com.lt.virtual_reflection

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.lt.virtual_reflection.bean.A
import com.lt.virtual_reflection.bean.B
import kotlin.reflect.KClass

//VirtualReflection
class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tv = TextView(this)
        setContentView(tv)
        initView(tv)
    }

    private fun initView(tv: TextView) {
        tv.text = A::class.newInstance().toString()
    }

    fun <T : Any> KClass<T>.newInstance(): T {
        return when (simpleName) {
            "A" -> A()
            "B" -> B()
            else -> throw RuntimeException("$this $simpleName: Not find in VirtualReflection config")
        } as T
    }

}


