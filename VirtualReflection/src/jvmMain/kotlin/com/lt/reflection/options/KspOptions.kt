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
    private val className = "classNameWithVirtualReflection"
    private val iosTargetName = "iosTargetNameWithVirtualReflection"

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

    /**
     * 生成的类名,默认VirtualReflectionUtil
     */
    fun getClassName(): String =
        options[className].ifNullOfEmpty { return "VirtualReflectionUtil" }

    /**
     * copy到iosMain的类名和方法名后缀,WithIOS
     * ps:由于ksp ios不支持iosMain,所以此作为临时方案
     */
    fun getIosTargetName(): String =
        options[iosTargetName].ifNullOfEmpty { return "WithIOS" }
}