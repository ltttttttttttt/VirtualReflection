package com.lt.reflection.options

/**
 * creator: lt  2022/10/22  lt.dygzs@qq.com
 * effect : 用于记录Type的信息
 * warning:
 */
internal class KSTypeInfo(
    val typeName: String,
    val nullable: String,
    val typeString: String,
) {
    override fun toString(): String {
        return "$typeName$typeString$nullable"
    }
}