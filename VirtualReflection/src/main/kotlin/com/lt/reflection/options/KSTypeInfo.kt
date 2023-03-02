package com.lt.reflection.options

import com.google.devtools.ksp.symbol.KSType

/**
 * creator: lt  2022/10/22  lt.dygzs@qq.com
 * effect : 用于记录Type的信息
 * warning:
 */
internal data class KSTypeInfo(
    val ksType: KSType,
    val isBuffBean: Boolean,
    val typeName: String,
    val nullable: String,
    val finallyTypeName: String,
    val typeString: String,
    val isList: Boolean,
)