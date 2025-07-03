package com.lt.reflection

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

/**
 * creator: lt  2022/10/21  lt.dygzs@qq.comS
 * effect : 工具类
 * warning:
 */

/**
 * 如果字符串为空或长度为0,就使用lambda中的字符串
 */
internal inline fun String?.ifNullOfEmpty(defaultValue: () -> String): String =
    if (this.isNullOrEmpty()) defaultValue() else this

/**
 * 打印日志
 */
internal fun String?.w(environment: SymbolProcessorEnvironment) {
    environment.logger.warn("lllttt VirtualReflection: ${this ?: "空字符串"}")
}

/**
 * 不会重复的add方法
 * ps:不能添加null,泛型类需要重写HashCode()和Equals()
 */
fun <T> MutableCollection<T>?.addNotRepeat(t: T?) {
    this ?: return
    t ?: return
    if (!this.contains(t))
        this.add(t)
}