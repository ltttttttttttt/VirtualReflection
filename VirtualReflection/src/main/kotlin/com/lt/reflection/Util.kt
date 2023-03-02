package com.lt.reflection

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.Nullability
import com.lt.reflection.options.KSTypeInfo
import com.lt.reflection.options.KspOptions
import java.io.OutputStream

/**
 * creator: lt  2022/10/21  lt.dygzs@qq.com
 * effect : 工具类
 * warning:
 */

internal val buffName = VirtualReflection::class.simpleName

/**
 * 向os中写入文字
 */
internal fun OutputStream.appendText(str: String) {
    this.write(str.toByteArray())
}

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
 * 获取ksType的信息
 * [ks] KSTypeReference信息
 * [options] 用户的配置
 * [isFirstFloor] 是否是最外层,用于判断泛型
 */
internal fun getKSTypeInfo(ks: KSTypeReference, options: KspOptions, isFirstFloor: Boolean = true): KSTypeInfo {
    //type对象
    val ksType = ks.resolve()
    //类是否有Buff注解
    val isBuffBean =
        ksType.declaration.annotations.toList()
            .find { it.shortName.getShortName() == buffName } != null
    //泛型中是否包含Buff注解
    var typeHaveBuff = false
    //是否是List<T>,后续在需要的地方会将List<T>转换为mutableStateListOf()
    var isList = false
    //泛型(只支持List<T>)
    val typeString =
        if (
            isFirstFloor
            && ksType.arguments.size == 1
            && ksType.toString().startsWith("List<")
            && ksType.arguments.first().type != null
        ) {
            isList = true
            //处理List<T>,支持转state,且自动加Buff
            val info = getKSTypeInfo(ksType.arguments.first().type!!, options, false)
            typeHaveBuff = info.isBuffBean
            val finallyTypeName = info.finallyTypeName
            if (finallyTypeName.isNotEmpty())
                "<$finallyTypeName>"
            else
                finallyTypeName
        } else if (ksType.arguments.isEmpty())
            ""//无泛型
        else {
            //其他泛型,原样输出
            ksType.arguments.filter { it.type != null }.joinToString(prefix = "<", postfix = ">") {
                val info = getKSTypeInfo(it.type!!, options, false)
                "${info.typeName}${info.typeString}${info.nullable}"
            }
        }
    //完整type字符串
    val typeName =
        "${ksType.declaration.packageName.asString()}.${ksType.declaration.simpleName.asString()}"
    //是否可空
    val nullable = if (ksType.nullability == Nullability.NULLABLE) "?" else ""
    //最后确定下来的type名字
    val finallyTypeName =
        if (isBuffBean) "$typeName${options.suffix}$typeString$nullable" else "$typeName$typeString$nullable"
    return KSTypeInfo(
        ksType,
        //自身或泛型包含Buff注解
        isBuffBean || typeHaveBuff,
        typeName,
        nullable,
        finallyTypeName,
        typeString,
        isList
    )
}