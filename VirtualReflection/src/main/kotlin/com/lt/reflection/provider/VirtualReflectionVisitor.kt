package com.lt.reflection.provider

import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.lt.reflection.getKSTypeInfo
import com.lt.reflection.options.KSClassConstructorInfo

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : 访问并处理相应符号
 * warning:
 */
internal class VirtualReflectionVisitor(
    private val environment: SymbolProcessorEnvironment,
    private val classConstructorList: ArrayList<KSClassConstructorInfo>
) : KSVisitorVoid() {

    /**
     * 访问class的声明
     */
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        //获取class信息并创建kt文件
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()
        classDeclaration.getConstructors()
            .filter { !it.isPrivate() }
            .forEach {
                classConstructorList.add(
                    KSClassConstructorInfo(
                        className,
                        packageName,
                        it.parameters.map {
                            getKSTypeInfo(it.type).toString()
                        },
                        classDeclaration.containingFile
                    )
                )
            }
    }
}