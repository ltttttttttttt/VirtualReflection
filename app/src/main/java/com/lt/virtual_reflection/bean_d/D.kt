package com.lt.virtual_reflection.bean_d

import com.lt.reflection.NotReflectionObjectConstructor

/**
 * creator: lt  2023/3/15  lt.dygzs@qq.com
 * effect :
 * warning:
 */
class D {
    private val content: String

    @NotReflectionObjectConstructor
    constructor(string: String, int: Int) {
        content = "$string  $int"
    }

    constructor(int: Int, string: String) {
        content = "$int  $string"
    }

    /**
     * Returns a string representation of the object.
     */
    override fun toString(): String {
        return content
    }
}