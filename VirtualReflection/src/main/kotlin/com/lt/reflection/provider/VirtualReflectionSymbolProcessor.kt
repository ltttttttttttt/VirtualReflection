package com.lt.reflection.provider

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.lt.reflection.w

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : ksp处理程序
 * warning:
 */
internal class VirtualReflectionSymbolProcessor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val ret = mutableListOf<KSAnnotated>()
        resolver.getAllFiles().forEach {
//            it.packageName.asString().w(environment)
            resolver.getDeclarationsInSourceOrder(it).forEach {
                if (it is KSClassDeclaration) {
                    if (it.classKind.type == "class") {
//                        it.simpleName.asString().w(environment)
                        if (!it.validate()) ret.add(it)
                        else it.accept(VirtualReflectionVisitor(environment), Unit)//处理符号
                    }
                }
            }
        }
        //返回无法处理的符号
        return ret
    }
}