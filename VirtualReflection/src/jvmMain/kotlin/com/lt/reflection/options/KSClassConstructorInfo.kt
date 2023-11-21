package com.lt.reflection.options

import com.google.devtools.ksp.symbol.KSFile

/**
 * creator: lt  2022/10/22  lt.dygzs@qq.com
 * effect : 用于记录KClass的构造信息
 * warning: equals判断全类名和构造函数信息
 */
internal data class KSClassConstructorInfo(
    val className: String,
    val packageName: String,
    val constructorArgsType: List<String>,
    val ksFile: KSFile?,
) {
    private val constructorArgsTypeCacheInfo = constructorArgsType.joinToString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KSClassConstructorInfo) return false

        if (className != other.className) return false
        if (packageName != other.packageName) return false
        if (constructorArgsTypeCacheInfo != other.constructorArgsTypeCacheInfo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = className.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + constructorArgsTypeCacheInfo.hashCode()
        return result
    }
}