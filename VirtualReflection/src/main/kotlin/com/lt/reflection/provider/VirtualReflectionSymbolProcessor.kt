package com.lt.reflection.provider

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.lt.reflection.appendText
import com.lt.reflection.options.KSClassConstructorInfo
import com.lt.reflection.options.KspOptions

/**
 * creator: lt  2022/10/20  lt.dygzs@qq.com
 * effect : ksp处理程序
 * warning:
 */
internal class VirtualReflectionSymbolProcessor(private val environment: SymbolProcessorEnvironment) :
    SymbolProcessor {
    //只做一轮处理
    private var isHandled = false

    @OptIn(KspExperimental::class)
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isHandled) return listOf()
        isHandled = true
        val packageList = KspOptions(environment).getPackageList()
        val ret = mutableListOf<KSAnnotated>()
        val classConstructorList = ArrayList<KSClassConstructorInfo>()
        resolver.getAllFiles()
            .filter {
                //包名如果是配置的或子包名
                val packageName = it.packageName.asString()
                packageList.any(packageName::contains)
            }.forEach { ksFile ->
                resolver.getDeclarationsInSourceOrder(ksFile).forEach {
                    if (it is KSClassDeclaration && it.classKind.type == "class") {
                        if (!it.validate()) ret.add(it)
                        else it.accept(
                            VirtualReflectionVisitor(environment, classConstructorList),
                            Unit
                        )//处理符号
                    }
                }
            }
        createFile(classConstructorList)
        //返回无法处理的符号
        return ret
    }

    private fun createFile(classConstructorList: ArrayList<KSClassConstructorInfo>) {
        val file = environment.codeGenerator.createNewFile(
            Dependencies(
                true,
                *classConstructorList.mapNotNull { it.ksFile }.toSet().toTypedArray()
            ), "", "VirtualReflectionUtil"
        )
        //记录有参数的构造方法,稍后处理
        val haveArgsConstructor = ArrayList<KSClassConstructorInfo>()
        file.appendText(
            "fun <T : Any> kotlin.reflect.KClass<T>.newInstance(): T {\n" +
                    "    return when (simpleName) {\n"
        )
        //处理空参构造方法
        classConstructorList.forEach {
            if (it.constructorArgsType.isNotEmpty()) {
                haveArgsConstructor.add(it)
                return@forEach
            }
            file.appendText("        \"${it.className}\" -> ${it.packageName}.${it.className}()\n")
        }
        file.appendText(
            "        else -> throw RuntimeException(\"\$this \$simpleName: Not find in VirtualReflection config\")\n" +
                    "    } as T\n" +
                    "}\n\n"
        )
        //处理有参构造方法
        file.appendText(
            "fun <T : Any> kotlin.reflect.KClass<T>.newInstance(vararg args: Any?): T {\n" +
                    "    return when {\n"
        )
        haveArgsConstructor.forEach {
            file.appendText(
                "        simpleName == \"${it.className}\" && args.size == ${it.constructorArgsType.size} -> ${it.packageName}.${it.className}("
            )
            //处理参数强转
            it.constructorArgsType.forEachIndexed { index, s ->
                file.appendText("args[$index] as $s, ")
            }
            file.appendText(")\n")
        }
        file.appendText(
            "        else -> throw RuntimeException(\"\$this \$simpleName: Not find in VirtualReflection config\")\n" +
                    "    } as T\n" +
                    "}"
        )
        file.close()
    }
}