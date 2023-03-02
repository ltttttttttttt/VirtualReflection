package com.lt.virtual_reflection.bean

/**
 * creator: lt  2023/3/2  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class B constructor() {
    var a = 0
    var b = ""

    constructor(a: Int, b: String?) : this() {
        this.a = a
        this.b = b ?: ""
    }

    constructor(a: Long) : this()

    override fun toString(): String {
        return "B(a=$a, b='$b')"
    }
}