package com.lt.reflection.options

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.lt.reflection.ifNullOfEmpty

/**
 * creator: lt  2022/10/23  lt.dygzs@qq.com
 * effect : 配置
 * warning:
 */
internal class KspOptions(environment: SymbolProcessorEnvironment) {
    private val options = environment.options
    private val packageList = "packageListWithVirtualReflection"
    private val functionName = "functionNameWithVirtualReflection"

    /**
     * 具体哪些包(完全相等)中的类需要虚拟反射功能,空格隔开
     */
    fun getPackageList(): List<String> =
        options[packageList].ifNullOfEmpty { return listOf() }
            .split(" ")
            .map { "$it." }

    /**
     * 生成的方法名,默认newInstance
     */
    fun getFunctionName(): String =
        options[functionName].ifNullOfEmpty { return "newInstance" }
}