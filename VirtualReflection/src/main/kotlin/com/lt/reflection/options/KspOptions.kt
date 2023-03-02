package com.lt.reflection.options

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.lt.reflection.ifNullOfEmpty

/**
 * creator: lt  2022/10/23  lt.dygzs@qq.com
 * effect : 配置
 * warning:
 */
internal class KspOptions(environment: SymbolProcessorEnvironment) {
    val suffix = "WithBuff"//后缀
    private val options = environment.options
    private val classSerializeAnnotation = "classSerializeAnnotation$suffix"
    private val fieldSerializeTransientAnnotation = "fieldSerializeTransientAnnotation$suffix"
    private val customInClass = "customInClass$suffix"
    private val customInFile = "customInFile$suffix"

    /**
     * 获取类序列化的注解
     */
    fun getClassSerializeAnnotation(): String =
        options[classSerializeAnnotation].ifNullOfEmpty { "@kotlinx.serialization.Serializable" }

    /**
     * 获取表示属性不参与序列化的注解
     */
    fun getFieldSerializeTransientAnnotation(): String =
        options[fieldSerializeTransientAnnotation].ifNullOfEmpty { "@kotlinx.serialization.Transient" }

    /**
     * 获取类中自定义的代码
     */
    fun getCustomInClass(getInfo: () -> CustomOptionsInfo): String {
        return handlerCustomCode(options[customInClass].ifNullOfEmpty { return "" }, getInfo())
    }

    /**
     * 获取文件中自定义的代码
     */
    fun getCustomInFile(getInfo: () -> CustomOptionsInfo): String {
        return handlerCustomCode(options[customInFile].ifNullOfEmpty { return "" }, getInfo())
    }

    /**
     * 处理自定义代码,将特殊字段替换为真实数据
     */
    private fun handlerCustomCode(code: String, info: CustomOptionsInfo): String {
        return code.replace("#originalClassName#", info.originalClassName)
            .replace("#className#", info.className)
    }
}