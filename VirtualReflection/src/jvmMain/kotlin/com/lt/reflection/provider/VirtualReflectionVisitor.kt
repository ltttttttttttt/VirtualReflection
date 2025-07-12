package com.lt.reflection.provider

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getConstructors
import com.google.devtools.ksp.isPrivate
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.symbol.Modifier
import com.lt.ksp.getKSTypeInfo
import com.lt.reflection.NotReflectionObjectConstructor
import com.lt.reflection.addNotRepeat
import com.lt.reflection.options.KSClassConstructorInfo

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : 访问并处理相应符号
 * warning:
 */
internal class VirtualReflectionVisitor(
    private val environment: SymbolProcessorEnvironment,
    private val classConstructorSet: MutableSet<KSClassConstructorInfo>
) : KSVisitorVoid() {

    /**
     * 访问class的声明
     */
    @OptIn(KspExperimental::class)
    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        //获取class信息并创建kt文件
        if (Modifier.PRIVATE in classDeclaration.modifiers) return
        if (Modifier.ABSTRACT in classDeclaration.modifiers) return
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.simpleName.asString()
        classDeclaration.getConstructors()
            .filter { !it.isPrivate() }
            .filter {
                it.getAnnotationsByType(NotReflectionObjectConstructor::class).toList().isEmpty()
            }
            .forEach {
                classConstructorSet.add(
                    KSClassConstructorInfo(
                        className,
                        packageName,
                        it.parameters.map {
                            getKSTypeInfo(it.type, null, classDeclaration).toString()
                        },
                        classDeclaration.containingFile
                    )
                )
            }
    }
}