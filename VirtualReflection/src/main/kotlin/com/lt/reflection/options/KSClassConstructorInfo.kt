package com.lt.reflection.options

import com.google.devtools.ksp.symbol.KSFile

/**
 * creator: lt  2022/10/22  lt.dygzs@qq.com
 * effect : 用于记录KClass的构造信息
 * warning:
 */
internal data class KSClassConstructorInfo(
    val className: String,
    val packageName: String,
    val constructorArgsType: List<String>,
    val ksFile: KSFile?,
)